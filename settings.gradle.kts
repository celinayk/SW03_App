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
        maven { url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")}
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")}
    }
}

rootProject.name = "SW03_APP"
include(":app")
