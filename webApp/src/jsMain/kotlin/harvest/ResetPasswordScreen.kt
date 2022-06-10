package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels.ResetPasswordDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.onChange
import react.router.dom.useSearchParams
import react.router.useNavigate
import react.useState

val ResetPasswordScreen = FC<Props> {
    var message by useState("")
    var changePassword by useState("")
    var password by useState("")
    val searchParams = useSearchParams()
    val navigator = useNavigate()

    val dataModel = ResetPasswordDataModel().apply {
        this.dataFlow.onEach { stateNew ->
            when (stateNew) {
                is PraxisDataModel.LoadingState -> {
                    message = "Loading..."
                }
                is PraxisDataModel.SuccessState<*> -> {
                    message = "Request Complete!"
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
            } }.launchIn(this.dataModelScope)
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

    TopAppBar {
        title = "Reset Password Form"
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
                    this.value = password
                    this.type = InputType.password
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                    this.placeholder = "New Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = changePassword
                    this.type = InputType.password
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        changePassword = target.value
                    }
                    this.placeholder = "Confirm Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                Button {
                    this.onClick = {
                        dataModel.resetPassword(
                            password,
                            token = searchParams.component1().get("token") ?: ""
                        )
                    }
                    +"Reset Password"
                }
            }
        }
    }

}