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
    dependencies{
        commonMainImplementation(enforcedPlatform(kotlinw("wrappers-bom:1.0.0-pre.340")))

        commonMainImplementation(kotlinw("react"))
        commonMainImplementation(kotlinw("react-dom"))
        commonMainImplementation(kotlinw("react-router-dom"))

        commonMainImplementation(kotlinw("emotion"))
        commonMainImplementation(kotlinw("mui"))
        commonMainImplementation(kotlinw("mui-icons"))

        commonMainImplementation(npm("date-fns", "2.28.0"))
        commonMainImplementation(npm("@date-io/date-fns", "2.14.0"))
    }
}