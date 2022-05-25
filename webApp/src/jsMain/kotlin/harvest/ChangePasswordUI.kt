package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.ChangePasswordDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.VFC
import react.dom.onChange
import react.useState

val ChangePasswordUI = VFC {
    var message by useState("")
    var changePassword by useState("")
    var state by useState<DataState>()
    var password by useState("")

    val dataModel = ChangePasswordDataModel(onDataState = { stateNew ->
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
        title = "Change Password Form"
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
                    this.placeholder = "Current Password"
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
                    this.placeholder = "New Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                Button {
                    this.onClick = {
                        dataModel.changePassWord(password, changePassword)
                    }
                    +"Change Password"
                }
            }
        }
    }
}