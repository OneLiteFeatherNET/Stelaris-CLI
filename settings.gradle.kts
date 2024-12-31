rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.0.21")
            version("minestom", "1.5.0")
            version("junit", "5.11.3")
            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("minestom")
            library("jetbrains.annotation", "org.jetbrains", "annotations").withoutVersion()
            library("microtus", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("dartpoet", "dev.themeinerlp", "dartpoet").version("0.0.1-SNAPSHOT")
            library("guava", "com.google.guava", "guava").version("33.3.1-jre")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").version("6.10.0.202406032230-r")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}