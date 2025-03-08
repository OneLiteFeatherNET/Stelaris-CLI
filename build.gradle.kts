plugins {
    alias(libs.plugins.kotlin)
    jacoco
}

group = "net.theevilreaper.stelaris.cli"
version = "0.0.3-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(platform(libs.microtus.bom))
    implementation(libs.dartpoet)
    implementation(libs.jetbrains.annotation)
    implementation(libs.microtus)
    implementation(libs.guava)
    implementation(libs.jgit)

    testImplementation(platform(libs.microtus.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    jacocoTestReport {
        dependsOn(rootProject.tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(true)
        }
    }

    test {
        finalizedBy(rootProject.tasks.jacocoTestReport)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
