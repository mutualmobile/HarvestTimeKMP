package orguser

import csstype.*
import harvest.material.TopAppBar
import kotlinx.datetime.internal.JSJoda.Clock
import kotlinx.datetime.internal.JSJoda.LocalDateTime
import mui.material.Box
import mui.material.Typography
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
    val today = LocalDateTime.now(Clock.systemDefaultZone())
    var selectedDate by useState(today)
    val navigate = useNavigate()
    var week by useState(mutableListOf<LocalDateTime>())
    val days = mutableListOf("Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun")

    fun generateWeek() {
        val localWeek = mutableListOf<LocalDateTime>()
        for (i in 1..7) {
            val first =  today.minusDays(today.dayOfWeek().ordinal()).minusDays(1).plusDays(i)
            localWeek.add(first)
        }
        week = localWeek
    }

    useEffectOnce {
        generateWeek()
    }

    Box {
        TopAppBar {
            title = "Time logging for ${Date()}"
            subtitle = message
        }

        mui.material.List {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.row
                padding = 0.px
            }
            week.mapIndexed { index, date ->
                DayOfWeekView {
                    weekDate = date.dayOfMonth().toInt()
                    isToday = date == today
                    isSelected = date.isEqual(selectedDate)
                    weekTitle = days[index]
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
}


val DayOfWeekView = FC<DayOfWeekViewProps> { props ->
    Box {
        component = ReactHTML.div
        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
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
    }
}