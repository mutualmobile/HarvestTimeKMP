package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.Routes.Keys.orgId
import com.mutualmobile.harvestKmp.datamodel.Routes.Keys.orgIdentifier
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.harvest.SignUpDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import react.router.dom.useSearchParams
import react.router.useNavigate


val JSSignupScreen = VFC {
    var message by useState("")
    var email by useState("")
    var firstName by useState("")
    var lastName by useState("")
    val searchParams = useSearchParams()

    val organizationIdentifier: String? = searchParams.component1().get(orgIdentifier)
    val orgId:String? = searchParams.component1().get(orgId)

    var orgName by useState("")
    var orgWebsite by useState("")
    var orgIdentifier by useState("")

    val navigator = useNavigate()

    var password by useState("")
    var confPassword by useState("")

    val dataModel = SignUpDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                val data = (stateNew.data as ApiResponse<*>)
                message = data.message ?: "No Data found!"
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
            }
            is ModalPraxisCommand -> {
                window.confirm(newCommand.title + "\n" + newCommand.message)
            }
        }
    }


    useEffectOnce {
        dataModel.activate()
    }

    TopAppBar {
        title = "${organizationIdentifier ?: ""} Signup Form"
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
                    this.value = firstName
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        firstName = target.value
                    }
                    this.placeholder = "First Name"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }
                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = lastName
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        lastName = target.value
                    }
                    this.placeholder = "Last Name"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
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

                if(organizationIdentifier == null){
                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = orgName
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            orgName = target.value
                        }
                        this.placeholder = "Org Name"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }

                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = orgWebsite
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            orgWebsite = target.value
                        }
                        this.placeholder = "Org Website"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }
                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = orgIdentifier
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            orgIdentifier = target.value
                        }
                        this.placeholder = "Org Identifier"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }


                }



                Button {
                    this.onClick = {
                        orgId?.let { organization ->
                            dataModel.signUp(firstName, lastName, organization, email, password)
                        } ?: run {
                            dataModel.signUp(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                orgName = orgName,
                                orgWebsite = orgWebsite,
                                orgIdentifier=orgIdentifier
                            )

                        }
                    }
                    +"Signup"
                }
            }
        }
    }
}