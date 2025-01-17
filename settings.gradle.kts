rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.1.0")
            version("minestom", "1.5.1")
            version("junit", "5.11.4")
            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("minestom")
            library("jetbrains.annotation", "org.jetbrains", "annotations").withoutVersion()
            library("microtus", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("dartpoet", "dev.themeinerlp", "dartpoet").version("0.0.1-SNAPSHOT")
            library("guava", "com.google.guava", "guava").version("33.4.0-jre")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").version("6.10.0.202406032230-r")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}