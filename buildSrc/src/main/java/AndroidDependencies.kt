object AndroidPluginDependencyVersions
object AndroidPluginDependencies {
  val plugins = listOf(
    "com.android.application" to "",
    "kotlin-android" to "",
    "com.google.gms.google-services" to "",
    "org.jetbrains.kotlin.android" to "",
  )
}

object AndroidDependencyVersions {
  const val material = "1.4.0"
  const val appCompat = "1.4.0"
  const val constraintLayout = "2.1.2"
  const val compose = "1.2.0-beta02"
  const val composeKotlinCompiler = compose
  const val coil = "2.0.0-rc03"
  const val lifecycleRuntime = "2.4.0"
  const val composeActivity = "1.4.0"
  const val ACCOMPANIST_VERSION = "0.24.6-alpha"
  const val firebaseBom = "30.0.0"
  const val splashScreen = "1.0.0-beta02"
  const val snapper = "0.2.1"
  const val navigation = "2.4.2"
}

object AndroidDependencies {
  val implementation = listOf(
    "com.google.accompanist:accompanist-insets:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "com.google.accompanist:accompanist-systemuicontroller:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "com.google.accompanist:accompanist-insets-ui:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "com.google.accompanist:accompanist-pager:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "com.google.accompanist:accompanist-pager-indicators:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "com.google.accompanist:accompanist-placeholder-material:${AndroidDependencyVersions.ACCOMPANIST_VERSION}",
    "dev.chrisbanes.snapper:snapper:${AndroidDependencyVersions.snapper}",
    "com.google.android.material:material:${AndroidDependencyVersions.material}",
    "androidx.appcompat:appcompat:${AndroidDependencyVersions.appCompat}",
    "androidx.constraintlayout:constraintlayout:${AndroidDependencyVersions.constraintLayout}",
    "androidx.compose.ui:ui:${AndroidDependencyVersions.compose}",
    "io.coil-kt:coil-compose:${AndroidDependencyVersions.coil}",
    "androidx.compose.material:material:${AndroidDependencyVersions.compose}",
    "androidx.compose.ui:ui-tooling-preview:${AndroidDependencyVersions.compose}",
    "androidx.lifecycle:lifecycle-runtime-ktx:${AndroidDependencyVersions.lifecycleRuntime}",
    "androidx.activity:activity-compose:${AndroidDependencyVersions.composeActivity}",
    "com.google.firebase:firebase-auth-ktx",
    "androidx.core:core-splashscreen:${AndroidDependencyVersions.splashScreen}",
    "androidx.navigation:navigation-compose:${AndroidDependencyVersions.navigation}",
    "org.jetbrains.kotlinx:kotlinx-datetime:${CommonDependencyVersions.kotlinxDateTime}",
    "io.insert-koin:koin-core:${CommonDependencyVersions.koin}",
    "io.insert-koin:koin-android:${CommonDependencyVersions.koin}"
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
