package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.OrgProjectDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.*
import mui.system.sx
import muix.pickers.*
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import react.router.useNavigate
import kotlin.js.Date
import kotlinext.js.require
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import mui.icons.material.CloseRounded
import mui.icons.material.DeleteForever
import mui.icons.material.Save


external interface CreateProjectProps : Props {
    var drawerOpen: Boolean
    var myKey:String
    var projectClicked: OrgProjectResponse?
    var onOpen: () -> Unit
    var onClose: () -> Unit
}


val JsCreateProject = FC<CreateProjectProps> { props ->
    var message by useState("")
    val navigator = useNavigate()
    var name by useState("")
    var client by useState("")
    var startDate by useState<Date>()
    var endDate by useState<Date?>(null)
    val format: dynamic = require("date-fns").format

    val dataModel = OrgProjectDataModel(onDataState = { stateNew ->
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
                message = "${response.message}"
                window.alert(message)
                props.onClose()
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
        message = ""
        dataModel.activate()
        startDate =
            if (props.projectClicked?.startDate != null) Date(props.projectClicked?.startDate!!.toString()) else Date()
        endDate =
            if (props.projectClicked?.endDate != null) Date(props.projectClicked?.endDate!!.toString()) else Date()
        name = props.projectClicked?.name ?: ""
        client = props.projectClicked?.client ?: ""
    }

    fun saveNow()  {
        props.projectClicked?.let {
            dataModel.updateProject(
                id = it.id!!,
                name = name,
                client = client,
                isIndefinite = endDate == null,
                startDate = format(startDate, "yyyy-MM-dd") as String,
                endDate = format(endDate, "yyyy-MM-dd") as? String,
            )
        } ?: run {
            dataModel.createProject(
                name = name,
                client = client,
                isIndefinite = endDate == null,
                startDate = format(startDate, "yyyy-MM-dd") as String,
                endDate = format(endDate, "yyyy-MM-dd") as? String
            )
        }
    }

    Drawer {
        key = props.myKey
        open = props.drawerOpen
        this.anchor = DrawerAnchor.bottom
        onClose = { event, reason ->
            props.onClose()
        }
        TopAppBar {
            title = "Create Project"
            subtitle = message

            props.projectClicked?.let { project ->
                IconButton {
                    DeleteForever {
                        onClick = {
                            dataModel.deleteProject(project.id!!)
                        }
                    }
                }
            }

            IconButton {
                CloseRounded {
                    onClick = {
                        props.onClose()
                    }
                }
            }

        }
        Box {
            Stack {
                sx {
                    margin = Margin(8.px, 8.px)
                }

                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = name
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        name = target.value
                        props.projectClicked?.name = name
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
                        props.projectClicked?.client = client
                    }
                    this.placeholder = "Client Name"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }

                Typography {
                    +"Start Date"
                }
                LocalizationProvider {
                    dateAdapter = AdapterDateFns
                    common.ui.CalendarPicker {
                        this.date = startDate
                        this.view = CalendarPickerView.day
                        this.onChange = { date, _ ->
                            if (date != null) {
                                startDate = date
                                props.projectClicked?.startDate =
                                    format(date, "yyyy-MM-dd") as String
                            }
                        }
                    }


                }
                Typography {
                    +"End Date"
                }
                LocalizationProvider {
                    dateAdapter = AdapterDateFns
                    common.ui.CalendarPicker {
                        this.date = endDate
                        this.view = CalendarPickerView.day
                        this.onChange = { date, _ ->
                            if (date != null) {
                                endDate = date
                                props.projectClicked?.endDate = format(date, "yyyy-MM-dd") as String
                            }
                        }
                    }
                }

                DialogActions {
                    Button {
                        onClick = {
                            saveNow()

                        }
                        props.projectClicked?.let {
                            +"Update Project"
                        } ?: run {
                            +"Create Project"
                        }

                    }
                    Button {
                        onClick = {
                            props.onClose()
                        }
                        +"Close"
                    }

                }
            }
        }
    }

}