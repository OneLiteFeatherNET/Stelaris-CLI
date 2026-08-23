import org.gradle.api.artifacts.result.ResolvedDependencyResult

plugins {
    jacoco
    application
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
}

group = "net.theevilreaper"
version = "1.21.8" // x-release-please-version

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.dartpoet)
    implementation(libs.minestom)
    implementation(libs.guava)
    implementation(libs.jgit)

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
// anything written down in this repo. `minestom.version` mirrors it so CI can
// see it, and this task is what keeps that mirror honest.
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
        archiveBaseName.set("stelaris-cli")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = "net.theevilreaper.stelaris.cli.StelarisCLIKt"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
