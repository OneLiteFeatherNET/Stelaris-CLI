# Guice-basierte Generator-Registrierung

Datum: 2026-08-28
Status: Entwurf zur Umsetzung freigegeben

## Problem

`GeneratorRegistry` hält alle 28 Generatoren in einer hartkodierten `setOf(...)`
(`GeneratorRegistry.kt:36`). Jeder neue Generator muss an zwei Stellen eingetragen
werden — Klasse anlegen und Registry ergänzen —, und ein vergessener Eintrag fällt
nirgends auf. Die Instanzen entstehen außerdem alle beim Konstruieren der Registry,
auch die, die der aktuelle Lauf gar nicht verwendet.

Ziel: Ein Generator wird allein dadurch registriert, dass er als Klasse existiert und
annotiert ist — Auto-Discovery wie `@ComponentScan` in Spring Boot, umgesetzt mit
Guice.

## Entscheidung

Auto-Discovery über den JDK-`ServiceLoader`, dessen Registrierungsdatei zur Compile-Zeit
von Googles `@AutoService`-Processor erzeugt wird. Guice bindet die gefundenen Klassen
über einen `Multibinder<GeneratorDescriptor>`.

Verworfene Alternativen:

- **Expliziter Multibinder**: verschiebt die Liste nur von der Registry ins Modul.
- **Classpath-Scan zur Laufzeit** (Guava `ClassPath`): erreicht dasselbe ohne
  Build-Änderung, stützt sich aber auf eine `@Beta`-API, kostet 30–100 ms Startzeit und
  muss gegen das Shadow-Jar abgesichert werden.
- **Eigener KSP-Processor**: einziger Ansatz, der eine vergessene Annotation zur
  Compile-Zeit meldet, verlangt dafür aber einen Multi-Module-Umbau und einen selbst
  gepflegten Processor.

`@AutoService` liefert den Nutzen des KSP-Ansatzes (Registrierung entsteht beim
Kompilieren, kein Laufzeit-Scan) ohne dessen Infrastrukturkosten.

## Architektur

### Komponenten

**`@CodeGenerator`** (vorhanden, wird angepasst) — trägt `name` und `experimental` und
ist alleinige Quelle dieser Metadaten. Die Retention wechselt von `BINARY` auf
`RUNTIME`, da `GeneratorModule` die Werte zur Laufzeit ausliest.

**`@AutoService(Generator::class)`** — veranlasst den Processor, die Klasse in
`META-INF/services/net.theevilreaper.stelaris.cli.generator.Generator` einzutragen.

**`GeneratorDescriptor`** (vorhanden, unverändert) — `name`, `experimental` und ein
`Provider<out Generator>`; `create()` erzeugt die Instanz erst bei Bedarf.

**`GeneratorModule`** (vorhanden, wird gefüllt) — liest die Service-Datei und bindet je
einen Descriptor in einen `Multibinder<GeneratorDescriptor>`:

```kotlin
class GeneratorModule : AbstractModule() {

    override fun configure() {
        val multibinder = Multibinder.newSetBinder(binder(), GeneratorDescriptor::class.java)
        ServiceLoader.load(Generator::class.java, javaClass.classLoader)
            .stream()
            .forEach { service ->
                val type = service.type()
                val metadata = requireNotNull(type.getAnnotation(CodeGenerator::class.java)) {
                    "${type.name} is registered as a Generator service but lacks @CodeGenerator"
                }
                multibinder.addBinding().toInstance(
                    GeneratorDescriptor(metadata.name, metadata.experimental, getProvider(type))
                )
            }
    }
}
```

`ServiceLoader` dient ausschließlich der Entdeckung: `service.type()` liefert die Klasse,
ohne sie zu instanziieren. Gebaut wird sie von Guice über `getProvider(type)`. Dadurch
bleibt die Lazy-Semantik erhalten, und Generatoren können später `@Inject`-Abhängigkeiten
erhalten, was ein direkter `ServiceLoader`-Aufruf ausschlösse.

**`GeneratorRegistry`** — bekommt das `Set<GeneratorDescriptor>` injiziert, prüft es beim
Konstruieren und filtert auf Descriptor-Ebene:

```kotlin
@Singleton
class GeneratorRegistry @Inject constructor(
    private val descriptors: Set<GeneratorDescriptor>,
) {
    init {
        check(descriptors.isNotEmpty()) {
            "No generators were discovered. When running from the shadow jar this usually " +
                "means mergeServiceFiles() is missing from the shadowJar task."
        }
        val duplicates = descriptors.groupBy { it.name }.filterValues { it.size > 1 }.keys
        check(duplicates.isEmpty()) { "Duplicate generator names: $duplicates" }
    }

    fun getDescriptors(): Set<GeneratorDescriptor> = descriptors

    fun createGenerators(predicate: (GeneratorDescriptor) -> Boolean = { true }): Set<Generator> =
        descriptors.filter(predicate).map { it.create() }.toSet()
}
```

### Datenfluss

1. Kompilieren: Der Processor sammelt alle `@AutoService(Generator::class)`-Klassen und
   schreibt die Service-Datei.
2. Start: `Guice.createInjector(GeneratorModule())` liest die Datei, legt pro Eintrag
   einen Descriptor an und bindet ihn.
3. Registry: prüft auf Leere und Namensdubletten — schlägt fehl, bevor irgendein
   Generator läuft.
4. `StelarisCLI`: filtert Descriptoren nach dem `-experimental`-Schalter und ruft
   `create()` nur für die verbleibenden auf.

Ein Nicht-Experimental-Lauf konstruiert experimentelle Generatoren damit nie — heute
werden alle 28 gebaut, unabhängig davon, ob sie verwendet werden.

## Änderungen im Einzelnen

### Generator-Vertrag

`Generator` behält `generate(outputPath)` und `cleanUp()`. `getName()` und
`isExperimental()` entfallen; die Metadaten liefert die Annotation über den Descriptor.
In `BaseGenerator` entfallen das `experimental`-Feld und der Import von
`org.jetbrains.annotations.ApiStatus`.

Beobachtung zur Ausgangslage: `getName()` wird in `src/main` an keiner Stelle aufgerufen —
nur 20 Tests prüfen damit den eigenen Klassennamen. Produktiv genutzt wird allein
`isExperimental()` in `StelarisCLI.kt:35`. Ebenso ruft kein Produktionscode `cleanUp()`
auf; das bleibt hier unangetastet, weil es nichts mit der DI-Umstellung zu tun hat.

Zweite Beobachtung: Kein Generator trägt derzeit `@ApiStatus.Experimental`. Der
`-experimental`-Schalter der CLI hat damit aktuell keine Wirkung. Das neue
`experimental`-Feld der Annotation erhält diese Fähigkeit für die Zukunft, ändert aber am
heutigen Verhalten nichts — alle 28 Generatoren werden mit `experimental = false`
annotiert.

### Alle 28 Generator-Klassen

Je Klasse: `@AutoService(Generator::class)` und `@CodeGenerator(name = "<Klassenname>")`
ergänzen, `override fun getName()` entfernen. Als `name` wird der bisherige
`getName()`-Rückgabewert übernommen (der einfache Klassenname), damit sich an Log- und
Fehlerausgaben nichts ändert.

### Einstiegspunkt

`StelarisCLI.kt:21` ersetzt `GeneratorRegistry()` durch den Injector; die Filterung
wandert auf den Descriptor:

```kotlin
val registry = Guice.createInjector(GeneratorModule()).getInstance(GeneratorRegistry::class.java)
val generators = registry.createGenerators { parsedArgs.experimental || !it.experimental }
```

Die bisherige Prüfung `if (generators.isEmpty())` mit der Meldung
`"The cli needs generators to run"` entfällt: Eine leere Menge ist kein Bedienfehler,
sondern ein Defekt, und wird schon beim Konstruieren der Registry gemeldet.

### Build

```kotlin
plugins { alias(libs.plugins.ksp) }

dependencies {
    implementation(libs.autoservice.annotations)  // com.google.auto.service:auto-service-annotations:1.1.1
    ksp(libs.autoservice.ksp)                     // dev.zacsweers.autoservice:auto-service-ksp:1.2.0
}

tasks.shadowJar { mergeServiceFiles() }
```

Version Catalog und Plugin-Deklaration kommen wie im Projekt üblich inline nach
`settings.gradle.kts`. `mergeServiceFiles()` fehlt heute und ist zwingend: ohne diesen
Aufruf enthält das Fat-Jar keine nutzbare Service-Datei und die CLI startet mit leerer
Registry.

Die Änderungen am Build werden vorab gegen die OLF-Gradle-Konvention
(`minestom-knowledge:gradle`) abgeglichen.

## Fehlerbehandlung

| Fehlerfall | Erkennung |
|---|---|
| Annotierte Klasse implementiert `Generator` nicht | Compile-Fehler durch den AutoService-Processor |
| Service-Klasse ohne `@CodeGenerator` | `CreationException` beim Injector-Bau |
| Doppelter Generator-Name | `IllegalStateException` im Registry-Konstruktor |
| Keine Generatoren gefunden | `IllegalStateException` mit Hinweis auf `mergeServiceFiles()` |
| `@AutoService` vergessen | nicht automatisch — siehe Vollständigkeitstest |

Der letzte Fall ist die bekannte Schwäche jedes Discovery-Ansatzes ohne eigenen Processor
und wird durch einen Test abgefangen statt durch den Compiler.

## Tests

**`GeneratorModuleTest`** (neu): baut einen echten Injector und prüft, dass Descriptoren
gefunden werden, alle Namen eindeutig und nicht leer sind und jeder Descriptor sich
instanziieren lässt.

**Vollständigkeitstest** (neu): sucht im Testklassenpfad mit Guavas `ClassPath` alle
`Generator`-Implementierungen unterhalb von `…generator.dart` und vergleicht sie mit den
gebundenen Descriptoren. Fehlt an einer Klasse die `@AutoService`-Annotation, schlägt
dieser Test fehl. Guava wird damit nur im Test verwendet, nicht im Produktionspfad.

**`verifyShadowJarServices`** (neuer Gradle-Task, an `build` gehängt): prüft, dass
`META-INF/services/net.theevilreaper.stelaris.cli.generator.Generator` im Shadow-Jar liegt
und so viele Einträge hat wie es Generatoren gibt. Sichert `mergeServiceFiles()` dauerhaft
ab.

**Anzupassen:** `GeneratorRegistryTest` (`assertEquals(28, …)` entfällt ersatzlos — die
Zahl kann nicht mehr driften), die 20 Generator-Tests mit
`assertEquals("XGenerator", generator.getName())` (Assert entfällt) und der
`Generator`-Fake in `LocalProjectExporterTest.kt:20` (Overrides für `getName()` und
`isExperimental()` entfernen).

## Risiken

**KSP mit Kotlin 2.4.10.** Seit Version 2.3.0 ist KSP von der Kotlin-Version entkoppelt
(aktuell 2.3.11); die Kombination ist aber noch nicht praktisch verifiziert. Erster
Umsetzungsschritt ist daher ein minimaler Build-Durchlauf mit KSP. Schlägt er fehl, ist
der Fallback `kapt("com.google.auto.service:auto-service:1.1.1")` mit Googles
Original-Processor — dieselbe Annotation, dasselbe Ergebnis, ohne KSP.

**Diff-Größe.** Angefasst werden 28 Generatoren, 22 Testdateien, Interface, Basisklasse,
Modul, Registry, Einstiegspunkt und Build. Die Schritte 3 und 4 der Reihenfolge unten sind
rein mechanisch und sollten getrennt committet werden.

## Umsetzungsreihenfolge

1. KSP-Plugin und AutoService-Abhängigkeiten ins Build, `mergeServiceFiles()` ergänzen,
   Kompatibilität mit einem Wegwerf-Generator verifizieren.
2. `@CodeGenerator` auf `RUNTIME` umstellen, `GeneratorModule` implementieren,
   `GeneratorRegistry` auf Descriptoren umbauen.
3. Alle 28 Generatoren annotieren, `getName()`-Overrides entfernen.
4. `Generator` und `BaseGenerator` verschlanken.
5. `StelarisCLI` auf den Injector umstellen.
6. Tests: neue Modul- und Vollständigkeitstests, bestehende Tests anpassen,
   `verifyShadowJarServices` ergänzen.
7. `./gradlew build` inklusive Shadow-Jar plus ein realer CLI-Lauf gegen ein
   Ausgabeverzeichnis als Gegenprobe.
