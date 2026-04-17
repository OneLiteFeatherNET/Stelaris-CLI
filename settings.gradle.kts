rootProject.name = "stelaris-cli"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
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
            version("kotlin", "2.3.20")
            version("bom", "1.6.4")
            version("junit", "6.0.3")
            version("dartpoet", "1.0.10")
            version("guava", "33.6.0-jre")
            version("jgit", "7.6.0.202603022253-r")
            version("shadow", "9.4.1")

            library("mycelium.bom", "net.onelitefeather", "mycelium-bom").versionRef("bom")
            library("minestom", "net.minestom", "minestom").withoutVersion()
            library("dartpoet", "net.theevilreaper", "dartpoet").versionRef("dartpoet")
            library("guava", "com.google.guava", "guava").versionRef("guava")
            library("jgit", "org.eclipse.jgit", "org.eclipse.jgit").versionRef("jgit")
            library("junit.jupiter", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.jupiter.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit.jupiter.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").version("6.0.3")

            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("shadow", "com.gradleup.shadow").versionRef("shadow")
        }
    }
}
