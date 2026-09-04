import org.gradle.api.artifacts.result.ResolvedDependencyResult
import java.util.zip.ZipFile

plugins {
    jacoco
    application
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.ksp)
}

group = "net.theevilreaper"
version = "1.21.8"

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.dartpoet)
    implementation(libs.minestom)
    implementation(libs.adventure.api)
    implementation(libs.guava)
    implementation(libs.jgit)
    implementation(libs.google.guice)
    implementation(libs.autoservice.annotations)
    ksp(libs.autoservice.ksp)

    testImplementation(libs.cyano)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass.set("net.theevilreaper.stelaris.cli.StelarisCLIKt")
}

// The Minecraft data the CLI generates comes from whatever Minestom version the
// dependency graph resolves to - which is the mycelium-bom's choice, not
// anything written down in this repo. This task makes it visible, so a release
// tag can be checked against the version it actually generates for.
val resolvedMinestomVersion: Provider<String> = configurations.named("runtimeClasspath").flatMap { configuration ->
    configuration.incoming.resolutionResult.rootComponent.map { root ->
        root.dependencies
            .filterIsInstance<ResolvedDependencyResult>()
            .mapNotNull { it.selected.moduleVersion }
            .firstOrNull { it.group == "net.minestom" && it.name == "minestom" }
            ?.version
            ?: error("net.minestom:minestom is not on the runtime classpath")
    }
}

tasks.register("resolveMinestomVersion") {
    description = "Prints the Minestom version the runtime classpath resolves to."
    val minestomVersion = resolvedMinestomVersion
    doLast {
        println(minestomVersion.get())
    }
}

// Generators are discovered through META-INF/services. Without mergeServiceFiles() in the
// shadowJar task that file does not survive into the fat jar and the CLI starts with an empty
// registry - a failure that only shows up when running the released artifact.
val verifyShadowJarServices = tasks.register("verifyShadowJarServices") {
    description = "Verifies that the shadow jar carries the generator service file."
    dependsOn(tasks.shadowJar)
    val jarFile = tasks.shadowJar.flatMap { it.archiveFile }
    val serviceEntry = "META-INF/services/net.theevilreaper.stelaris.cli.generator.Generator"
    doLast {
        ZipFile(jarFile.get().asFile).use { jar ->
            val entry = jar.getEntry(serviceEntry)
                ?: error("$serviceEntry is missing from the shadow jar. Is mergeServiceFiles() still set?")
            val generators = jar.getInputStream(entry).bufferedReader().readLines()
                .filter { it.isNotBlank() }
            check(generators.isNotEmpty()) { "$serviceEntry is empty in the shadow jar" }
            logger.lifecycle("Shadow jar registers ${generators.size} generators")
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
    }

    shadowJar {
        // The generator registry is discovered through META-INF/services, so the
        // service files of every dependency have to survive the merge into the fat jar.
        mergeServiceFiles()
        archiveBaseName.set("stelaris-cli")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = "net.theevilreaper.stelaris.cli.StelarisCLIKt"
        }
    }

    build {
        dependsOn(shadowJar, verifyShadowJarServices)
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
