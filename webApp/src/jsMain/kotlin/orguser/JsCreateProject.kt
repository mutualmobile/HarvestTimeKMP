package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.harvest.CreateProjectDataModel
import csstype.Color
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.system.responsive
import mui.system.sx
import muix.pickers.DatePicker
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.useNavigate

external interface CreateProjectProps : Props {
    var drawerOpen: Boolean
    var onOpen: () -> Unit
    var onClose: () -> Unit
}


val JsCreateProject = FC<CreateProjectProps> { props ->
    var message by useState("")
    val navigator = useNavigate()

    var name by useState("")
    var client by useState("")
    val isIndefinite by useState(false)
    var startDate by useState("")
    var endDate by useState("")

    val dataModel = CreateProjectDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is Complete -> {
                message = "Complete!"
            }
            is EmptyState -> {
                message = "Initial State!"
            }
            is ErrorState -> {
                message = "Error State ${stateNew.throwable.message ?: "Error"}"
            }
            is SuccessState<*> -> {
                val response = stateNew.data as ApiResponse<*>
                message = "create project ${response.message}"

            }
        }
    })

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


    Drawer {
        this.variant = DrawerVariant.temporary
        this.anchor = DrawerAnchor.bottom
        open = props.drawerOpen
        onClose = { event, reason ->
            props.onClose()
        }
        sx {
            backgroundColor = Color("main")
        }

        Box {
            component = ReactHTML.nav
            TopAppBar {
                title = "Create Project"
                subtitle = message
            }
            Divider {}
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
                        this.value = name
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            name = target.value
                        }
                        this.placeholder = "Project Name"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }

                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = client
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            client = target.value
                        }
                        this.placeholder = "Client Name"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }

                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = startDate
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            startDate = target.value
                        }
                        this.placeholder = "startDate"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }
                    TextField {
                        this.variant = FormControlVariant.outlined
                        this.value = endDate
                        this.onChange = {
                            val target = it.target as HTMLInputElement
                            endDate = target.value
                        }
                        this.placeholder = "endDate"
                        sx {
                            margin = Margin(12.px, 2.px)
                        }
                    }



                    FormControlLabel {
                        control = Checkbox.create().apply {
                            this.props.checked = isIndefinite
                        }
                        label = ReactNode("Is Indefinite ?")
                    }

                    Stack {
                        this.direction = responsive(StackDirection.row)

                        if (isIndefinite) {
                            // don't show end date
                        } else {
                            //show end date
                        }
                    }


                    Button {
                        variant = ButtonVariant.contained
                        this.onClick = {
                            dataModel.createProject(
                                name = name,
                                client = client,
                                isIndefinite = isIndefinite,
                                startDate = startDate,
                                endDate = endDate
                            )
                        }
                        +"Create Project"
                    }
                }
            }
        }
    }
}