package orguser.timelogging

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDate
import mainScope
import mui.icons.material.ArrowBackIos
import mui.icons.material.ArrowForwardIos
import mui.icons.material.Check
import mui.lab.TabContext
import mui.lab.TabPanel
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.useNavigate
import kotlin.Float
import kotlin.js.Date

fun generateWeek(date: LocalDate): MutableList<LocalDate> {
    val localWeek = mutableListOf<LocalDate>()
    for (i in 1..7) {
        val first = date.minusDays(date.dayOfWeek().ordinal()).minusDays(1).plusDays(i)
        localWeek.add(first)
    }
    return localWeek
}

val format: dynamic = kotlinext.js.require("date-fns").format

val JsTimeLoggingScreen = FC<Props> {

    val today = LocalDate.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    var week by useState {
        generateWeek(selectedDate)
    }
    var showTimeLogDialog by useState(false)
    val navigator = useNavigate()

    var projects by useState<List<OrgProjectResponse>>()
    var work by useState<List<HarvestUserWorkResponse>>()
    var isLoadingWeekRecords by useState(false)
    var projectId by useState<String>()

    var note by useState("")
    var workHours by useState(0.00f)
    val dataModel = TimeLogginDataModel()

    val userId = dataModel.userId

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

    useEffect(dependencies = arrayOf(week,showTimeLogDialog), effect = {
        userId?.let { userId ->
            fetchWeekRecords(
                dataModel,
                format(
                    Date(week.first().toString()),
                    "yyyy-MM-dd"
                ) as String, format(
                    Date(week.last().toString()),
                    "yyyy-MM-dd"
                ) as String, userId
            ) { dataState: DataState ->
                isLoadingWeekRecords = dataState is LoadingState
                if (dataState is SuccessState<*>) {
                    val data = dataState.data
                    if (data is ApiResponse<*>) {
                        work = data.data as List<HarvestUserWorkResponse>
                    }
                }
            }
        }
    })

    useEffectOnce {
        dataModel.activate()
        dataModel.getUserAssignedProjects(userId).onEach { dataState: DataState ->
            when (dataState) {
                is SuccessState<*> -> {
                    val successData = dataState.data
                    if (successData is ApiResponse<*>) {
                        if (successData.data is List<*>) {
                            projects = successData.data as List<OrgProjectResponse>
                        }
                    }
                }
                else -> {}
            }
        }.launchIn(mainScope)
    }

    Box {

        Container {
            sx {
                padding = 20.px
                borderRadius = 10.px
                marginBottom = 10.px
            }

            Stack {
                this.direction = responsive(StackDirection.row)

                IconButton {
                    ArrowBackIos {

                    }
                    onClick = {
                        week = generateWeek(week.first().minusWeeks(1))
                    }
                }

                IconButton {
                    ArrowForwardIos {

                    }
                    onClick = {
                        week = generateWeek(week.first().plusWeeks(1))
                    }
                }

                Typography {
                    variant = TypographyVariant.h6
                    this.component = ReactHTML.div
                    +"${format(Date(selectedDate.toString()), "MMMM d LLLL")}"
                }
            }




            Box {

                sx {
                    margin = 12.px
                    display = Display.flex
                    justifyContent = JustifyContent.left
                    flexDirection = FlexDirection.row
                }

                Stack {
                    direction = responsive(StackDirection.column)
                    NewEntryButton {
                        clicked = {
                            showTimeLogDialog = true
                        }
                    }
                    Typography {
                        sx {
                            marginTop = 4.px
                        }
                        variant = TypographyVariant.subtitle2
                        +"New Entry"
                    }
                }
                TabContext {
                    value = format(Date(selectedDate.toString()), "do iii") as String
                    Stack {
                        direction = responsive(StackDirection.column)
                        Box {
                            sx {
                                borderBottom = 1.px
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
                                        label = ReactNode(start)
                                    }
                                }
                            }

                        }
                        week.mapIndexed { item, date ->
                            TabPanel {
                                value = format(Date(date.toString()), "do iii") as String

                                DayContent {
                                    selectDate = date
                                    selectDateString = format(
                                        Date(selectedDate.toString()),
                                        "yyyy-MM-dd"
                                    ) as String
                                    isLoading = isLoadingWeekRecords
                                    workWeek = work
                                    assignedProjects = projects
                                }
                            }
                        }
                    }
                }


            }


        }

        BackdropTimeLogger {
            this.dataModel = dataModel
            this.showTimeLogDialog = showTimeLogDialog
            this.selectedDate = selectedDate
            this.projects = projects
            this.projectId = projectId
            this.userId = userId
            this.projectIdNew = { it ->
                projectId = it
            }
            this.workHours = workHours
            this.onWorkHours = {
                workHours = it
            }
            this.note = note
            this.onNote = {
                note = it
            }
            this.closeDialog = {
                showTimeLogDialog = false
            }
        }


    }
}

fun fetchWeekRecords(
    dataModel: TimeLogginDataModel,
    first: String,
    last: String,
    userId: String,
    function: (DataState) -> Unit
) {
    dataModel.getWorkLogsForDateRange(first, last, listOf(userId)).onEach {
        function(it)
    }.launchIn(dataModel.dataModelScope)
}


