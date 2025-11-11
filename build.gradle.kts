plugins {
    jacoco
    application
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
}

group = "net.theevilreaper"
version = "0.0.3-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.dartpoet)
    implementation(libs.minestom)
    implementation(libs.guava)
    implementation(libs.jgit)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass.set("net.theevilreaper.stelaris.cli.StelarisCLIKt")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
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
