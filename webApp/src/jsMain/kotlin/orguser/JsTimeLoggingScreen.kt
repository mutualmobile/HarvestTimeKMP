package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLoggingDataModel
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
    val dataModel = TimeLoggingDataModel()

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

    useEffect(dependencies = arrayOf(week), effect = {
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
                                    selectDateString = format(Date(selectedDate.toString()), "yyyy-MM-dd") as String
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
                    SaveTimeButton {
                        onDone = {
                            showTimeLogDialog = false
                        }
                        saveProjectId = projectId
                        saveuserId = userId
                        this.dataModel = dataModel
                        date = format(
                            Date(selectedDate.toString()),
                            "yyyy-MM-dd"
                        ) as String
                        this.note = note
                        this.workHours = workHours
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

fun fetchWeekRecords(
    dataModel: TimeLoggingDataModel,
    first: String,
    last: String,
    userId: String,
    function: (DataState) -> Unit
) {
    dataModel.getWorkLogsForDateRange(first, last, listOf(userId)).onEach {
        function(it)
    }.launchIn(dataModel.dataModelScope)
}

external interface NewEntryButtonProps : Props {
    var clicked: () -> Unit
}

external interface SaveTimeButtonProps : Props {
    var saveProjectId: String?
    var saveuserId: String?
    var date: String
    var note: String
    var workHours: Float
    var dataModel: TimeLoggingDataModel
    var onDone: () -> Unit
}

val SaveTimeButton = FC<SaveTimeButtonProps> { props ->
    var isSaving by useState(false)
    if (isSaving) {
        CircularProgress()
    } else {
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
                props.saveProjectId?.let {
                    props.saveuserId?.let {
                        props.dataModel.logWorkTime(
                            HarvestUserWorkRequest(
                                null,
                                props.saveProjectId.toString(),
                                props.saveuserId.toString(),
                                props.date,
                                props.workHours,
                                props.note
                            )
                        ).onEach {
                            isSaving = it is LoadingState
                            println(it)
                            when (it) {
                                is SuccessState<*> -> {
                                    props.onDone.invoke()
                                }
                            }
                        }.launchIn(props.dataModel.dataModelScope)
                    } ?: run {
                        println("userid null")
                    }
                } ?: run {
                    println("projectId null")
                }
            }
        }
    }

}

val NewEntryButton = FC<NewEntryButtonProps> { props ->
    Card {
        sx {
            width = 40.px
            height = 40.px
            backgroundColor = NamedColor.darkgreen
            alignItems = AlignItems.center
            display = Display.flex
            borderRadius = 6.px

        }
        mui.icons.material.Add() {
            sx {
                color = NamedColor.white
                width = 40.px
                height = 40.px
            }
            onClick = {
                props.clicked()
            }
        }
    }
}

external interface DayContentProps : Props {
    var selectDate: LocalDate
    var selectDateString: String
    var isLoading: Boolean
    var workWeek: List<HarvestUserWorkResponse>?
    var assignedProjects: List<OrgProjectResponse>?
}

val DayContent = FC<DayContentProps> { props ->
    Container {
        sx {
            width = 100.pct
            minHeight = 25.pct
        }

        if (props.isLoading) {
            CircularProgress()
        }

        if (props.workWeek.isNullOrEmpty()) {
            Typography {
                +"Time expands, then contracts, all in tune with the stirrings of the heart.\n– Haruki Murakami"
            }
        } else {
            List {
                props.workWeek?.filter {format(Date(it.workDate), "yyyy-MM-dd") as String == props.selectDateString }
                    ?.map { work ->
                        val projectName =
                            props.assignedProjects?.firstOrNull { it.id == work.id }?.name ?: ""
                        // TODO We don't want to do this, instead get the project name form the api response
                        ListItemText {
                            primary = "Project: $projectName ".toReactNode()
                            secondary =
                                "Time: ${work.workHours} Note: ${work.note.toString()}".toReactNode()
                        }
                    }
            }
        }


    }
}

private fun String.toReactNode(): ReactNode {
    return ReactNode(this)
}
