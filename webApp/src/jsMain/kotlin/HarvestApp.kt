import components.ThemeModule
import harvest.*
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.FC
import react.Props
import react.create
import react.router.dom.BrowserRouter
import workspace.JsWorkspaceFindScreen

external interface AppProps : Props

val HarvestApp = FC<AppProps> {
    BrowserRouter {
        ThemeModule {
            Routes {
                Route {
                    path = "*"
                    this.element = JSNotFound.create()
                }
                Route {
                    this.index = true
                    this.element = JsWorkspaceFindScreen.create()
                    this.path = "/"
                }
                Route {
                    this.path = "/login"
                    this.element = JSLoginScreen.create()
                }
                Route {
                    this.path = "/changePassword"
                    this.element = ChangePasswordUI.create()
                }
                Route {
                    this.path = "/trendingui"
                    this.element = TrendingUI.create()
                }
                Route {
                    this.path = "/signup"
                    this.element = JSSignupScreen.create()
                }
            }
        }
    }
}