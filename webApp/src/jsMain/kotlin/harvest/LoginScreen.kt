package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.js.jso
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.dom.html.InputType
import react.router.NavigateFunction
import react.router.dom.useSearchParams
import react.router.useNavigate
import setupFcmPush


val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    val searchParams = useSearchParams();

    val organizationId: String? = searchParams.component1().get(HarvestRoutes.Keys.orgIdentifier)
    val orgId: String? = searchParams.component1().get(HarvestRoutes.Keys.orgId)

    var password by useState("")
    var isLoading by useState(false)
    val navigator = useNavigate()

    val dataModel = LoginDataModel(onDataState = { stateNew ->
        isLoading = stateNew is LoadingState
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = (stateNew.data as LoginResponse).message ?: "Some message"
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

    dataModel.praxisCommand = { newCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
                setupFcmPush()
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
        title = "${organizationId ?: ""} Login Form"
        subtitle = message
    }
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
                this.type = InputType.email
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
                this.type = InputType.password
                this.onChange = {
                    val target = it.target as HTMLInputElement
                    password = target.value
                }
                this.placeholder = "Enter Password"
                sx {
                    margin = Margin(12.px, 2.px)
                }
            }

            if (isLoading) {
                CircularProgress()
            } else {
                Button {
                    this.variant = ButtonVariant.contained
                    sx {
                        this.margin = Margin(12.px, 4.px)
                    }
                    this.onClick = {
                        dataModel.login(email, password)
                    }
                    +"Login"
                }
            }


            Button {
                sx {
                    this.margin = Margin(12.px, 4.px)
                }
                onClick = {
                    navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.FORGOT_PASSWORD)
                }
                +"Forgot Password"
            }

            organizationId?.takeIf { it.isNotEmpty() }?.let {
                Button {
                    sx {
                        this.margin = Margin(12.px, 4.px)
                    }
                    +"Signup with $organizationId"
                    onClick = {
                        navigator(
                            BROWSER_SCREEN_ROUTE_SEPARATOR +
                                    HarvestRoutes.Screen.SIGNUP.withOrgId(organizationId, orgId)
                        )
                    }
                }
            }
        }
    }
}

fun navigateRoot(navigator: NavigateFunction) {
    navigator.invoke(to = BROWSER_SCREEN_ROUTE_SEPARATOR, options = jso {
        replace = true
    })
}