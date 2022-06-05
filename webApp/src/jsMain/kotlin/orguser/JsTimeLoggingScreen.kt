package orguser

import csstype.*
import harvest.material.TopAppBar
import kotlinx.css.whiteAlpha
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDate
import kotlinx.datetime.internal.JSJoda.LocalDateTime
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.router.useNavigate
import kotlin.js.Date

val JsTimeLoggingScreen = FC<Props> {
    var message by useState("")
    val today = LocalDate.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    val navigate = useNavigate()
    var week by useState(mutableListOf<LocalDate>())
    var showTimeLogDialog by useState(false)
    val days = mutableListOf("Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun")
    val format: dynamic = kotlinext.js.require("date-fns").format

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
                    multiline = true
                    rows = 4
                    placeholder = "Notes (Optional)"
                    variant = FormControlVariant.outlined
                }

                TextField {
                    placeholder = "0.00"
                    type = InputType.number
                    variant = FormControlVariant.outlined
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
                        }
                    }

                    Button {
                        variant = ButtonVariant.outlined
                        Typography {
                            +"Cancel"
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
        }
        mui.icons.material.Add() {
            sx {
                color = NamedColor.white
                width = 30.px
                height = 30.px
                alignSelf = AlignSelf.center
                padding = 4.px
            }
            onClick = {
                props.clicked()
            }
        }
    }
}


