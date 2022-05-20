import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies
import harvest.JSHomePage
import harvest.JSLoginScreen
import harvest.JSSignupScreen
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import react.create
import react.createElement
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter


val sharedComponent = SharedComponent()

fun main() {
    initSqlDelightExperimentalDependencies()
    window.onload = { _ ->
        val rootDiv = document.getElementById("root")
        render(rootDiv!!) {
            BrowserRouter {
                Routes {
                    Route {
                        attrs.index = true
                        attrs.element = createElement {
                            JSHomePage.create()
                        }
                    }
                    Route {
                        attrs.path = "/login"
                        attrs.element = createElement {
                            JSLoginScreen.create()
                        }
                    }
                    Route {
                        attrs.path = "/signup"
                        attrs.element = createElement {
                            JSSignupScreen.create()
                        }
                    }
                }
            }
            child(JSLoginScreen)
        }
    }
}

suspend fun setupDriver() {
    sharedComponent.provideGithubTrendingLocal().driver?.let {} ?: run {
        setupDriverInternal()
    }

}

private suspend fun setupDriverInternal() {
    try {
        val driver = DriverFactory().createDriverBlocking()
        val trendingLocal = sharedComponent.provideGithubTrendingLocal()
        trendingLocal.driver = driver
    } catch (ex: Exception) {
        console.log(ex.message)
        console.log("Exception happened here.")
        console.log(ex)
    }
}
