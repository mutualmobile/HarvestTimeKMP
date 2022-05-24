package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import common.Themes
import components.AppThemeContext
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.MediaQueryList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.*
import react.dom.*
import react.dom.aria.ariaLabel
import react.router.useNavigate


val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    var state by useState<DataState>()
    var password by useState("")
    var appTheme by useContext(AppThemeContext)
    val navigator = useNavigate()

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
        dataModel.activate()
    }

    TopAppBar {
        title = "Login Form"
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