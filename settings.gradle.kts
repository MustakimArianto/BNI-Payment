pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

apply(from = "${rootDir}/maven-credentials.gradle.kts")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "IntegraRepository"
            url = uri("https://webadmin.integra-pratama.co.id/integra-maven-repo/releases")

            credentials {
                username = extra["integraMavenUsername"]?.toString()
                password = extra["integraMavenPassword"]?.toString()
            }
        }
    }
}

rootProject.name = "BNI Payment"
include(":app")
include(":sdk")
