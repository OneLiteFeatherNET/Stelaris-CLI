rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.0.20")
            version("junit", "5.10.2")
            library("jetbrains.annotation", "org.jetbrains", "annotations").version("24.1.0")
            library("microtus", "net.onelitefeather.microtus", "Minestom").version("1.3.1")
            library("dartpoet", "dev.themeinerlp", "dartpoet").version("0.0.1-SNAPSHOT")
            library("guava", "com.google.guava", "guava").version("33.1.0-jre")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").version("6.9.0.202403050737-r")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}