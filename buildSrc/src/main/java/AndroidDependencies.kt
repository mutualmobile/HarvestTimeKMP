object AndroidPluginDependencyVersions
object AndroidPluginDependencies {
  val plugins = listOf(
    "com.android.application" to "",
    "kotlin-android" to "",
    "com.google.gms.google-services" to "",
  )
}

object AndroidDependencyVersions {
  const val material = "1.4.0"
  const val appCompat = "1.4.0"
  const val constraintLayout = "2.1.2"
  const val compose = "1.1.0-rc01"
  const val composeKotlinCompiler = "1.1.0-rc02"
  const val coil = "1.4.0"
  const val lifecycleRuntime = "2.4.0"
  const val composeActivity = "1.4.0"
  const val ACCOMPANIST_VERSION = "0.24.1-alpha"
  const val firebaseBom = "30.0.0"
}

object AndroidDependencies {
  private const val ACCOMPANIST_INSETS =
    "com.google.accompanist:accompanist-insets:${AndroidDependencyVersions.ACCOMPANIST_VERSION}"
  private const val ACCOMPANIST_SYSTEM_UI_CONTROLLER =
    "com.google.accompanist:accompanist-systemuicontroller:${AndroidDependencyVersions.ACCOMPANIST_VERSION}"
  val implementation = listOf(
    ACCOMPANIST_INSETS, ACCOMPANIST_SYSTEM_UI_CONTROLLER,
    "com.google.android.material:material:${AndroidDependencyVersions.material}",
    "androidx.appcompat:appcompat:${AndroidDependencyVersions.appCompat}",
    "androidx.constraintlayout:constraintlayout:${AndroidDependencyVersions.constraintLayout}",
    "androidx.compose.ui:ui:${AndroidDependencyVersions.compose}",
    "io.coil-kt:coil-compose:${AndroidDependencyVersions.coil}",
    "androidx.compose.material:material:${AndroidDependencyVersions.compose}",
    "androidx.compose.ui:ui-tooling-preview:${AndroidDependencyVersions.compose}",
    "androidx.lifecycle:lifecycle-runtime-ktx:${AndroidDependencyVersions.lifecycleRuntime}",
    "androidx.activity:activity-compose:${AndroidDependencyVersions.composeActivity}",
    "com.google.firebase:firebase-auth-ktx"
  )
  val androidTestImplementation = listOf(
    "androidx.compose.ui:ui-test-junit4:${AndroidDependencyVersions.compose}"
  )
  val debugImplementation = listOf(
    "androidx.compose.ui:ui-tooling:${AndroidDependencyVersions.compose}"
  )
  val platforms = listOf(
    "com.google.firebase:firebase-bom:${AndroidDependencyVersions.firebaseBom}"
  )
}

object AndroidMainDependencies {
  val implementation = listOf(
    "com.squareup.sqldelight:android-driver:${CommonDependencyVersions.sqlDelight}",
    "io.ktor:ktor-client-android:${CommonDependencyVersions.ktor}",
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${CommonDependencyVersions.coroutines}",
  )
}

object AndroidTestDependencies {
  val implementation = listOf(
    "junit:junit:${CommonDependencyVersions.junit}"
  )
  val kotlin = listOf(
    "test-junit"
  )
}
