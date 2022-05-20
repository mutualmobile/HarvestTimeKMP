import common.ThemeModule
import common.Themes
import harvest.JSHomePage
import harvest.JSLoginScreen
import harvest.JSNotFound
import harvest.JSSignupScreen
import mui.system.Theme
import react.*
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter


external interface AppProps : Props

val HarvestApp = FC<AppProps> {

    BrowserRouter {
        ThemeModule {
            Routes {
                Route {
                    this.element = JSNotFound.create()
                }
                Route {
                    this.index = true
                    this.element = JSHomePage.create()
                    this.path = "/"
                }
                Route {
                    this to "/login"
                    this.path = "/login"
                    this.element = JSLoginScreen.create()
                }
                Route {
                    this to "/trendingui"
                    this.path = "/trendingui"
                    this.element = TrendingUI.create()
                }
                Route {
                    this.path = "/signup"
                    this.element = JSSignupScreen.create()
                    this to "/signup"
                }
            }
        }
    }
}