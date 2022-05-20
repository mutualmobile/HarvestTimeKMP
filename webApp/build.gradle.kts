plugins {
    WebAppPlugins.plugins.forEach { dependency ->
        kotlin(dependency)
    }
}

kotlin {
    js(IR) {
        browser{
            runTask {
                devServer = devServer?.copy(port = 3000)
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":shared"))
                WebAppDependencies.implementation.forEach(::implementation)
                WebAppDependencies.kotlin.map { dependency ->
                    kotlin(dependency)
                }.forEach(::implementation)
                WebAppDependencies.npm.map { (dependency, version) ->
                    npm(dependency, version)
                }.forEach(::implementation)
            }
        }
    }
}
