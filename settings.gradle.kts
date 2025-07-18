rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven {
            name = "OneLiteFeatherRepository"
            url = uri("https://repo.onelitefeather.dev/onelitefeather")
            if (System.getenv("CI") != null) {
                credentials {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            } else {
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.2.0")
            version("bom", "1.4.2")
            version("junit", "5.13.3")
            version("snakeyaml", "2.4")
            version("dartpoet", "0.0.1-SNAPSHOT")
            version("guava", "33.4.8-jre")
            version("jgit", "7.3.0.202506031305-r")

            library("mycelium.bom", "net.onelitefeather", "mycelium-bom").versionRef("bom")
            library("minestom", "net.minestom", "minestom").withoutVersion()
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