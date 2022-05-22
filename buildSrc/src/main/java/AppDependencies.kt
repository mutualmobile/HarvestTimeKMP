object AppDependencyVersions {
    const val kotlinGradle = "1.6.21"
    const val androidGradle = "7.2.0"
    const val kotlinxSerialization = "1.6.10"
    const val sqlDelight = "1.5.3"
    const val nativeCoroutines = "0.12.1-new-mm"
    const val googleServices = "4.3.10"
}
object AppDependencies {
    val plugins = listOf(
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${AppDependencyVersions.kotlinGradle}",
        "com.android.tools.build:gradle:${AppDependencyVersions.androidGradle}",
        "org.jetbrains.kotlin:kotlin-serialization:${AppDependencyVersions.kotlinxSerialization}",
        "com.squareup.sqldelight:gradle-plugin:${AppDependencyVersions.sqlDelight}",
        "com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:${AppDependencyVersions.nativeCoroutines}",
        "com.google.gms:google-services:${AppDependencyVersions.googleServices}"
    )
}
