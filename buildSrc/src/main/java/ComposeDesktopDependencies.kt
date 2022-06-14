object ComposeDesktopDependencies {
  val implementation = listOf(
    "com.squareup.sqldelight:sqlite-driver:${CommonDependencyVersions.sqlDelight}",
    "io.ktor:ktor-client-java:${CommonDependencyVersions.ktor}",
  )
}

object ComposeDesktopPlugins {
  val plugins = listOf(
    "org.jetbrains.compose" to CommonPlugins.composeGradlePlugin,
  )
  val kotlin = listOf("jvm" to AppDependencyVersions.kotlinGradle)
}
