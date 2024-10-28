plugins {
    alias(libs.plugins.kotlin)
    jacoco
}

group = "net.theevilreaper.stelaris.cli"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(platform(libs.microtus.bom))
    implementation(libs.dartpoet)
    implementation(libs.jetbrains.annotation)
    implementation(libs.microtus)
    implementation(libs.guava)
    implementation(libs.jgit)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
