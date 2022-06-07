package orguser

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.LogWorkTimeDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDate
import kotlinx.js.jso
import mui.icons.material.Check
import mui.material.*
import mui.material.MuiList.Companion.dense
import mui.material.styles.TypographyVariant
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.dom.useSearchParams
import react.router.useNavigate
import kotlin.js.Date

val JsTimeLoggingScreen = FC<Props> {

    var isLoading by useState(false)
    var message by useState("")
    val today = LocalDate.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    val navigate = useNavigate()
    var week by useState(mutableListOf<LocalDate>())
    var showTimeLogDialog by useState(false)
    val days = mutableListOf("Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun")
    val format: dynamic = kotlinext.js.require("date-fns").format
    val navigator = useNavigate()
    var projects by useState<List<OrgProjectResponse>>()
    var projectId by useState<String>()
    var userId by useState<String>()
    val mainScope = MainScope()

    var note by useState("")
    var workHours by useState(0.00f)

    val dataModel = LogWorkTimeDataModel(onDataState = { dataState: DataState ->
        isLoading = dataState is LoadingState
        when (dataState) {
            is SuccessState<*> -> {
                try {
                    val successData = dataState.data
                    if (successData is ApiResponse<*>) {
                        if (successData.data is List<*>) {
                            projects = successData.data as List<OrgProjectResponse>
                            projectId = projects?.firstOrNull()?.id
                        }
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            is ErrorState -> {
                message = dataState.throwable.message.toString()
            }
            is LoadingState -> {
                message = "Loading..."
            }
            else -> {}
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

    fun generateWeek() {
        val localWeek = mutableListOf<LocalDate>()
        for (i in 1..7) {
            val first = today.minusDays(today.dayOfWeek().ordinal()).minusDays(1).plusDays(i)
            localWeek.add(first)
        }
        week = localWeek
    }

    useEffectOnce {
        dataModel.activate()
        userId = dataModel.getUser()?.uid
        dataModel.getUserAssignedProjects(userId)
        generateWeek()
    }

    Box {
        Typography {
            variant = TypographyVariant.h6
            this.component = ReactHTML.div
            +"Time logging for ${format(Date(selectedDate.toString()), "MMMM d LLLL")}"
        }

        Container {
            sx {
                padding = 20.px
                borderRadius = 10.px
                marginBottom = 10.px
            }

            NewEntryButton {
                clicked = {
                    showTimeLogDialog = true
                }
            }
            Tabs {
                indicatorColor = TabsIndicatorColor.primary
                value = week.indexOf(selectedDate)
                variant = TabsVariant.scrollable
                scrollButtons = TabsScrollButtons.auto
                onChange = { event, newValue ->
                    selectedDate = week[newValue]
                }

                week.mapIndexed { index, date ->
                    val start =
                        format(Date(date.toString()), "do iii") as String
                    Tab {
                        value = index
                        label = ReactNode(start)
                    }
                }
            }

        }

        Backdrop {
            open = showTimeLogDialog

            Card {
                sx {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    padding = 24.px
                    borderRadius = 6.px
                }
                Typography {
                    sx {
                        display = Display.flex
                        flexGrow = number(1.0)
                        paddingBottom = 8.px
                    }
                    variant = TypographyVariant.h6
                    this.component = ReactHTML.div
                    +"New time entry for ${format(Date(selectedDate.toString()), "MMMM d LLLL")}"
                }
                Typography {
                    sx {
                        display = Display.flex
                        flexGrow = number(1.0)
                        paddingBottom = 8.px
                    }
                    variant = TypographyVariant.h5
                    this.component = ReactHTML.div
                    +"Project / Task"
                }

                projects?.let {
                    MenuList {
                        projects?.map { project ->
                            MenuItem {
                                if (project.id == projectId) {
                                    ListItemIcon {
                                        Check {

                                        }
                                    }
                                }
                                value = project.id
                                ListItemText {
                                    primary = ReactNode(project.name ?: "")
                                }
                                onClick = {
                                    projectId = project.id
                                    println(projectId)
                                }
                            }
                        }
                    }
                } ?: run {
                    MenuItem {
                        value = ""
                        ListItemText {
                            primary = ReactNode("Projects not available yet")
                        }
                        onClick = {
                        }
                    }
                }


                TextField {
                    sx {
                        padding = 4.px
                    }
                    placeholder = "Notes (Optional)"
                    variant = FormControlVariant.outlined
                    value = note
                    onChange = {
                        val target = it.target as HTMLInputElement
                        note = target.value
                    }
                }

                TextField {
                    sx {
                        padding = 4.px
                    }
                    placeholder = "0.00"
                    type = InputType.number
                    variant = FormControlVariant.outlined
                    value = workHours
                    onChange = {
                        val target = it.target as HTMLInputElement
                        workHours = target.value.toFloat()
                    }
                }

                Box {
                    sx {
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        padding = 4.px
                    }
                    Button {
                        variant = ButtonVariant.contained
                        Typography {
                            +"Save Time"
                        }
                        sx {
                            paddingRight = 6.px
                            paddingTop = 4.px
                            paddingLeft = 4.px
                        }
                        onClick = {
                            projectId?.let {
                                userId?.let {
                                    dataModel.logWorkTime(
                                        HarvestUserWorkRequest(
                                            null,
                                            projectId.toString(),
                                            userId.toString(),
                                            format(
                                                Date(selectedDate.toString()),
                                                "yyyy-MM-dd"
                                            ) as String,
                                            workHours,
                                            note
                                        )
                                    ).onEach {
                                        println(it)
                                        when (it) {
                                            is SuccessState<*> -> {
                                                showTimeLogDialog = false
                                            }
                                        }
                                    }.launchIn(mainScope)
                                }?:run{
                                    println("userid null")
                                }
                            }?:run{
                                println("projectId null")
                            }
                        }
                    }

                    Button {
                        sx {
                            padding = 4.px
                        }
                        variant = ButtonVariant.outlined
                        Typography {
                            +"Cancel"
                        }
                        onClick = {
                            showTimeLogDialog = false
                        }
                    }
                }


            }
        }
    }
}

external interface NewEntryButtonProps : Props {
    var clicked: () -> Unit
}

val NewEntryButton = FC<NewEntryButtonProps> { props ->
    Card {
        sx {
            width = 40.px
            height = 40.px
            backgroundColor = NamedColor.darkgreen
            alignItems = AlignItems.center
            display = Display.flex
        }
        mui.icons.material.Add() {
            sx {
                color = NamedColor.white
                width = 30.px
                height = 30.px
                alignSelf = AlignSelf.center
            }
            onClick = {
                props.clicked()
            }
        }
    }
}

external interface ProjectAutoComplete {
    var label: String
    var id: String
}


