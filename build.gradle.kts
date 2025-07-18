plugins {
    alias(libs.plugins.kotlin)
    jacoco
}

group = "net.theevilreaper"
version = "0.0.3-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.dartpoet)
    implementation(libs.snakeyaml)
    implementation(libs.minestom)
    implementation(libs.guava)
    implementation(libs.jgit)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
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

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
