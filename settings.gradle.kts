pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VMAX"

include(
    ":common",
    ":core-model",
    ":core-validation",
    ":core-intelligence",
    ":core-workflow",
    ":core-action",
    ":core-security",
    ":runtime",
    ":payment-engine",
    ":app"
)
