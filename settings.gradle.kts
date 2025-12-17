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
            version("kotlin", "2.3.0")
            version("bom", "1.5.3")
            version("junit", "6.0.1")
            version("dartpoet", "0.0.1-SNAPSHOT")
            version("guava", "33.5.0-jre")
            version("jgit", "7.5.0.202512021534-r")
            version("shadow", "9.3.0")

            library("mycelium.bom", "net.onelitefeather", "mycelium-bom").versionRef("bom")
            library("minestom", "net.minestom", "minestom").withoutVersion()
            library("dartpoet", "dev.themeinerlp", "dartpoet").versionRef("dartpoet")
            library("guava", "com.google.guava", "guava").versionRef("guava")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").versionRef("jgit")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.jupiter.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").version("6.0.1")

            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("shadow", "com.gradleup.shadow").versionRef("shadow")
        }
    }
}