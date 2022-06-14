object CommonDependencyVersions {
    const val multiplatformSettings = "0.9"
    const val sqlDelight = "1.5.3"
    const val ktor = "2.0.1"
    const val kotlinxDateTime = "0.3.2"
    const val kotlinxSerialization = "1.3.2"
    const val coroutines = "1.6.1"
    const val koin = "3.1.4"
    const val junit = "4.13.2"
    const val mokoResources = AppDependencyVersions.mokoResources
}

object CommonMainDependencies {
    val implementation = listOf(
        "com.russhwolf:multiplatform-settings-no-arg:${CommonDependencyVersions.multiplatformSettings}",
        "com.russhwolf:multiplatform-settings:${CommonDependencyVersions.multiplatformSettings}",
        "com.squareup.sqldelight:runtime:${CommonDependencyVersions.sqlDelight}",
        "io.ktor:ktor-client-core:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-client-json:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-client-auth:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-client-content-negotiation:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-serialization-kotlinx-json:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-client-logging:${CommonDependencyVersions.ktor}",
        "io.ktor:ktor-client-serialization:${CommonDependencyVersions.ktor}",
        "org.jetbrains.kotlinx:kotlinx-datetime:${CommonDependencyVersions.kotlinxDateTime}",
        "org.jetbrains.kotlinx:kotlinx-serialization-core:${CommonDependencyVersions.kotlinxSerialization}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${CommonDependencyVersions.coroutines}",
    )

    val api = listOf(
        "io.insert-koin:koin-core:${CommonDependencyVersions.koin}",
        "dev.icerock.moko:resources:${CommonDependencyVersions.mokoResources}",
    )
}

object CommonTestDependencies {
    val implementation = listOf(
        "com.russhwolf:multiplatform-settings-test:${CommonDependencyVersions.multiplatformSettings}",
    )
    val kotlin = listOf(
        "test-common",
        "test-annotations-common"
    )
}

object CommonPlugins {
    val plugins = listOf(
        "com.android.library",
        "kotlinx-serialization",
        "com.squareup.sqldelight",
        "com.rickclephas.kmp.nativecoroutines",
        "dev.icerock.mobile.multiplatform-resources",
    )
    val kotlinPlugins = listOf(
        "multiplatform",
        "native.cocoapods"
    )
    const val composeGradlePlugin = "1.2.0-alpha01-dev709"
}
