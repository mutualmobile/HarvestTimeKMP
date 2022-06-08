package orguser.timelogging

import com.mutualmobile.harvestKmp.datamodel.*
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
import mui.material.*
import mui.system.sx
import react.*
import react.router.useNavigate
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
    var work by useState<MutableList<HarvestUserWorkResponse>>()
    var isLoadingWeekRecords by useState(false)
    var projectId by useState<String>()

    var note by useState("")
    var workHours by useState(0.00f)
    var workId by useState<String?>(null)
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

    useEffect(dependencies = arrayOf(week, showTimeLogDialog), effect = {
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
                        work = data.data as MutableList<HarvestUserWorkResponse>
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

            WeekSwitcher {
                this.selectedDate = selectedDate
                this.currentWeek = week
                this.onWeekChange = {
                    week = it
                }
            }






            Box {

                sx {
                    margin = 12.px
                    display = Display.flex
                    justifyContent = JustifyContent.left
                    flexDirection = FlexDirection.row
                }

                JSNewTimeEntryButton {
                    this.onClicked = {
                        showTimeLogDialog = true
                    }
                }

                TabsWeekDays {
                    this.selectedDate = selectedDate
                    this.isLoadingWeekRecords = isLoadingWeekRecords
                    this.week = week
                    this.work = work
                    this.projects = projects
                    this.onDateChange = {
                        selectedDate = it
                    }
                    this.onDeleteRequested = {
                        dataModel.deleteWork(it).onEach { dataState ->
                            isLoadingWeekRecords = dataState is LoadingState
                            if (dataState is SuccessState<*>) {
                                showTimeLogDialog = false
                                work?.remove(it)
                            }
                        }.launchIn(dataModel.dataModelScope)
                    }
                    this.onEditRequested = {
                        it.note?.let {
                            note = it
                        }
                        workId = it.id
                        workHours = it.workHours
                        projectId = it.projectId
                        showTimeLogDialog = true
                    }
                }
            }


        }

        Backdrop {
            open = showTimeLogDialog
            BackdropTimeLogger {
                this.dataModel = dataModel
                this.selectedDate = selectedDate
                this.projects = projects
                this.projectId = projectId
                this.userId = userId
                this.workId = workId
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
                    workId = null
                    note = ""
                    workHours = 0f
                    projectId = null
                    showTimeLogDialog = false
                }
                this.closeDialogOnSave = {
                    workId = null
                    note = ""
                    workHours = 0f
                    projectId = null
                    showTimeLogDialog = false
                }
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


