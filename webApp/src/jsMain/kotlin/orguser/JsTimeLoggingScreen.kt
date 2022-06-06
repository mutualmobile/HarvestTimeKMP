package orguser

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.LogWorkTimeDataModel
import csstype.*
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDate
import mui.material.*
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

    val searchParams = useSearchParams()
    val userId: String? = searchParams.component1().get(HarvestRoutes.Keys.id)
    val projectId: String? = searchParams.component1().get(HarvestRoutes.Keys.id)

    var isLoading by useState(false)
    var message by useState("")
    val today = LocalDate.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    val navigate = useNavigate()
    var week by useState(mutableListOf<LocalDate>())
    var showTimeLogDialog by useState(false)
    val days = mutableListOf("Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun")
    val format: dynamic = kotlinext.js.require("date-fns").format
    var res by useState<Unit>()
    val navigator = useNavigate()

    var note by useState("")
    var workHours by useState(0.00f)

    val dataModel = LogWorkTimeDataModel(onDataState = { dataState: DataState ->
        isLoading = dataState is LoadingState
        when (dataState) {
            is SuccessState<*> -> {
                try {
                    val response = (dataState.data as ApiResponse<Unit>)
                    res = response.data
                } catch (ex: Exception){
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
                /*TODO*/
            }
            is ModalPraxisCommand -> {
                /*TODO*/
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
                    variant = TypographyVariant.h4
                    this.component = ReactHTML.div
                    +"Project / Task"
                }

                @Suppress("UPPER_BOUND_VIOLATED")
                Autocomplete<AutocompleteProps<String>> {
                    disablePortal = true
                    sx {
                        width = 300.px
                        padding = 4.px
                    }
                    options = arrayOf("Project 1", "Project 2", "Project 3")
                    renderInput = { params ->
                        TextField.create {
                            +params
                            label = ReactNode("Projects")
                        }
                    }
                }

                TextField {
                    sx {
                        padding = 4.px
                    }
                    multiline = true
                    rows = 4
                    this.placeholder = "Notes (Optional)"
                    this.variant = FormControlVariant.outlined
                    this.value = note
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        note = target.value
                    }
                }

                TextField {
                    sx {
                        padding = 4.px
                    }
                    multiline = true
                    rows = 4
                    this.placeholder = "0.00"
                    this.type = InputType.number
                    this.variant = FormControlVariant.outlined
                    this.value = workHours
                    this.onChange = {
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
                        sx {
                            padding = 4.px
                        }
                        variant = ButtonVariant.contained
                        Typography {
                            +"Save Time"
                        }
                        sx {
                            paddingRight = 6.px
                        }
                        onClick = {
                            showTimeLogDialog = false
                            dataModel.logWorkTime(
                                null,
                                projectId.toString(),
                                userId.toString(),
                                format(Date(selectedDate.toString()), "MMMM d LLLL") as String,
                                workHours,
                                note
                            )
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


