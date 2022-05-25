package harvest

import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import react.router.useLocation
import react.router.useNavigate
import kotlin.js.Json

external interface SignupProps : Props

val JSSignupScreen = FC<SignupProps> {
    var message by useState("")
    var email by useState("")
    var organization by useState<String?>(null)
    var password by useState("")
    var confPassword by useState("")
    val navigator = useNavigate()
    val location = useLocation()


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
        title = "${organization?:""} Signup Form"
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

                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = confPassword
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        confPassword = target.value
                    }
                    this.placeholder = "Confirm Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                Button {
                    this.onClick = {
                        dataModel.signup(email, password)
                    }
                    +"Signup"
                }
            }
        }
    }
}