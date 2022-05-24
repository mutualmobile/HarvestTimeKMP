package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import common.Themes
import components.AppThemeContext
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.js.jso
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.MediaQueryList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.*
import react.dom.*
import react.dom.aria.ariaLabel
import react.router.NavigateFunction
import react.router.NavigateOptions
import react.router.useLocation
import react.router.useNavigate
import kotlin.js.Json


val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    var state by useState<DataState>()
    var organization by useState("")
    var password by useState("")
    var appTheme by useContext(AppThemeContext)
    val navigator = useNavigate()
    val location = useLocation()

    val dataModel = LoginDataModel(onDataState = { stateNew ->
        state = stateNew
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = "Logged In!"
                navigator.invoke(to = "/trendingui")
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Empty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })


    useEffectOnce {
        try {
            val orgJson = location.state.unsafeCast<Json>()
            orgJson["orgId"]?.let {
                organization = it.toString()
                dataModel.activate()
            } ?: kotlin.run {
                navigateRoot(navigator)
            }
        } catch (ex: Exception) {
            navigateRoot(navigator)
        }

    }

    TopAppBar {
        title = "$organization Login Form"
        subtitle = message
    }
    Paper {
        Card {
            sx {
                margin = Margin(24.px, 24.px)
            }
            Stack {
                sx {
                    margin = Margin(24.px, 24.px)
                }
                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = email
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        email = target.value
                    }
                    this.placeholder = "Work Email"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = password
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                    this.placeholder = "Enter Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                Button {
                    onClick = {
                        appTheme = if (appTheme == Themes.Light) Themes.Dark else Themes.Light
                    }
                    +"Change Theme!"
                }

                Button {
                    this.onClick = {
                        dataModel.login(email, password)
                    }
                    +"Login"
                }
            }
        }
    }
}

private fun navigateRoot(navigator: NavigateFunction) {
    navigator.invoke(to = "/", options = jso {
        replace = true
    })
}