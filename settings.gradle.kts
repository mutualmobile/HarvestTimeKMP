pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "HarvestKMP"
include(":androidApp")
include(":shared")
include(":webApp")
include(":compose-desktop")