package orguser

import csstype.*
import harvest.material.TopAppBar
import kotlinx.css.whiteAlpha
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDate
import kotlinx.datetime.internal.JSJoda.LocalDateTime
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import kotlin.js.Date

val JsTimeLoggingScreen = FC<Props> {
    var message by useState("")
    val today = LocalDate.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    val navigate = useNavigate()
    var week by useState(mutableListOf<LocalDate>())
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
            +"Time logging for ${Date()}"
        }

        Container {
            sx {
                background = NamedColor.white
                padding = 20.px
                borderRadius = 10.px
                marginBottom = 10.px
            }

            ToggleButtonGroup{
                color = ToggleButtonGroupColor.primary
                value = selectedDate
                exclusive = true
                onChange = { event, newDate->
                    selectedDate = newDate
                }

                week.mapIndexed { index, date ->
                    val start =
                        format(Date(date.toString()), "do iii") as String
                    ToggleButton{
                        value = date
                        +start
                    }
                }


            }
        }
    }
}

external interface DayOfWeekViewProps : Props {
    var weekDate: Int
    var isToday: Boolean
    var isSelected: Boolean
    var weekTitle: String
    var onSelected: ()->Unit
}


val DayOfWeekView = FC<DayOfWeekViewProps> { props ->
    Box {
        component = ReactHTML.div
        onClick = {
            props.onSelected()
        }

        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            borderRadius = 25.px
            margin = 12.px
        }
        Typography {
            +props.weekTitle
            sx {
                padding = Padding(4.px, 4.px)
            }
        }
        Typography {
            +props.weekDate.toString()
            sx {
                padding = Padding(4.px, 4.px)
            }
        }
        if(props.isSelected){
            Box{
                sx {
                    width = 10.px
                    height = 10.px
                    borderRadius = 25.px
                    color = NamedColor.darkred
                }
            }
        }
    }
}