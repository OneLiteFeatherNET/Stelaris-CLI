rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.1.21")
            version("minestom", "1.5.1")
            version("junit", "5.13.3")
            version("snakeyaml", "2.4")
            version("dartpoet", "0.0.1-SNAPSHOT")
            version("guava", "33.4.8-jre")
            version("jgit", "7.3.0.202506031305-r")

            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("minestom")
            library("jetbrains.annotation", "org.jetbrains", "annotations").withoutVersion()
            library("microtus", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("dartpoet", "dev.themeinerlp", "dartpoet").versionRef("dartpoet")
            library("guava", "com.google.guava", "guava").versionRef("guava")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").versionRef("jgit")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.jupiter.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").version("1.13.3")
            library("snakeyaml", "org.yaml", "snakeyaml").versionRef("snakeyaml")

            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}