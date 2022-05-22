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

object WebAppDependencies {
    val implementation = listOf(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${WebAppDependencyVersions.coroutines}",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${WebAppDependencyVersions.kotlinxSerialization}",
        "org.jetbrains.kotlinx:kotlinx-html-js:${WebAppDependencyVersions.kotlinxHtmlJs}",
        "org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.5-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-react:18.0.0-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.0.0-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.0-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-mui-icons:5.6.2-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-mui:5.6.2-pre.332-kotlin-1.6.21",
        "org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.332-kotlin-1.6.21",
    )
    val kotlin = listOf(
        "stdlib-js"
    )
    val npm = listOf(
        "copy-webpack-plugin" to "9.0.0"
    )
}

object WebAppPlugins {
    val plugins = listOf(
        "multiplatform",
        "plugin.serialization",
    )
}
