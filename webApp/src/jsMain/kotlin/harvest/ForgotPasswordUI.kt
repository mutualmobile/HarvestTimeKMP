package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels.ForgotPasswordDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.VFC
import react.dom.onChange
import react.router.useNavigate
import react.useEffectOnce
import react.useState

val ForgotPasswordUI = VFC {
    var message by useState("")
    var email by useState("")
    val navigator = useNavigate()

    val dataModel = ForgotPasswordDataModel().apply {
        this.dataFlow.onEach { stateNew ->
            when (stateNew) {
                is PraxisDataModel.LoadingState -> {
                    message = "Loading..."
                }
                is PraxisDataModel.SuccessState<*> -> {
                    message = "Request Complete"
                    navigator.invoke(to = BROWSER_SCREEN_ROUTE_SEPARATOR)
                }
                PraxisDataModel.Complete -> {
                    message = "Completed loading!"
                }
                PraxisDataModel.EmptyState -> {
                    message = "Empty state"
                }
                is PraxisDataModel.ErrorState -> {
                    message = stateNew.throwable.message ?: "Error"
                }
            }
        }.launchIn(this.dataModelScope)
    }

    dataModel.praxisCommand = { newCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }

    useEffectOnce {
        dataModel.activate()
    }

    TopAppBar {
        title = "Forgot Password Form"
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

                Button {
                    this.onClick = {
                        dataModel.forgotPassword(email)
                    }
                    +"Request Reset Password"
                }
            }
        }
    }
}