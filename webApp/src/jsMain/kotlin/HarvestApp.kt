import components.ThemeModule
import harvest.JSHomePage
import harvest.JSLoginScreen
import harvest.JSNotFound
import harvest.JSSignupScreen
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.FC
import react.Props
import react.create
import workspace.JsWorkspaceFindScreen

external interface AppProps : Props

val HarvestApp = FC<AppProps> {
    HashRouter {
        ThemeModule {
            Routes {
                Route {
                    this.element = JSNotFound.create()
                }
                Route {
                    this.index = true
                    this.element = JsWorkspaceFindScreen.create()
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