package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.ResetPasswordDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.onChange
import react.useState

val ResetPasswordScreen = FC<Props> {
    var message by useState("")
    var changePassword by useState("")
    var password by useState("")

    val dataModel = ResetPasswordDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = "Request Complete!"
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
                        dataModel.resetPassword(password, token = "") // TODO get this from url!
                    }
                    +"Reset Password"
                }
            }
        }
    }

}