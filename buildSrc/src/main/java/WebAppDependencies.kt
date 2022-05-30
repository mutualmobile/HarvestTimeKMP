object WebAppDependencyVersions {
    const val kotlinxSerialization = "1.3.0"
    const val kotlinxHtmlJs = "0.7.5"
    const val coroutines = CommonDependencyVersions.coroutines
}

object WebAppCommonMainDependencies {
    val implementation = listOf(
        "io.ktor:ktor-client-js:${CommonDependencyVersions.ktor}",
        "com.squareup.sqldelight:sqljs-driver:${CommonDependencyVersions.sqlDelight}",
    )
}

fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

object WebAppDependencies {
    val implementation = listOf(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${WebAppDependencyVersions.coroutines}",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${WebAppDependencyVersions.kotlinxSerialization}",
        "org.jetbrains.kotlinx:kotlinx-html-js:${WebAppDependencyVersions.kotlinxHtmlJs}",
        "org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.5-pre.332-kotlin-1.6.21",
    )
    val kotlin = listOf(
        "stdlib-js"
    )
    val npm = listOf(
        "copy-webpack-plugin" to "9.0.0",
        "firebase" to "7.24.0",
        "date-fns" to "2.28.0",
        "@date-io/date-fns" to "2.14.0"
    )
}

object WebAppPlugins {
    val plugins = listOf(
        "multiplatform",
        "plugin.serialization",
    )
}
