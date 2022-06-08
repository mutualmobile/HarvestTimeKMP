package orguser.timelogging

import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import csstype.px
import kotlinx.datetime.internal.JSJoda.LocalDate
import mui.lab.TabContext
import mui.lab.TabPanel
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import kotlin.js.Date

external interface TabsWeekDaysProps : Props {
    var onEditRequested: (HarvestUserWorkResponse)->Unit
    var onDeleteRequested: (HarvestUserWorkResponse)->Unit
    var onDateChange: (LocalDate) -> Unit
    var projects: List<OrgProjectResponse>?
    var work: List<HarvestUserWorkResponse>?
    var isLoadingWeekRecords: Boolean
    var selectedDate: LocalDate
    var week: List<LocalDate>
}

val TabsWeekDays = FC<TabsWeekDaysProps> { props ->
    TabContext {
        value = format(Date(props.selectedDate.toString()), "do iii") as String
        Stack {
            direction = responsive(StackDirection.column)
            Stack {
                direction = responsive(StackDirection.row)
                sx {
                    borderBottom = 1.px
                }
                Tabs {
                    indicatorColor = TabsIndicatorColor.primary
                    value = props.week.indexOf(props.selectedDate)
                    variant = TabsVariant.scrollable
                    scrollButtons = TabsScrollButtons.auto
                    onChange = { event, newValue ->
                        props.onDateChange.invoke(props.week[newValue])
                    }

                    props.week.mapIndexed { index, date ->
                        val start =
                            format(Date(date.toString()), "do iii") as String
                        Tab {
                            label = ReactNode(start)
                        }
                    }
                }

                JSWeekTotal {
                    this.workWeek = props.work
                }

            }
            props.week.mapIndexed { item, date ->
                TabPanel {
                    value = format(Date(date.toString()), "do iii") as String
                    DayContent {
                        selectDate = date
                        selectDateString = format(
                            Date(props.selectedDate.toString()),
                            "yyyy-MM-dd"
                        ) as String
                        isLoading = props.isLoadingWeekRecords
                        workWeek = props.work
                        assignedProjects = props.projects
                        onEditRequested = {
                            props.onEditRequested.invoke(it)
                        }
                        onDeleteRequested = {
                            props.onDeleteRequested.invoke(it)
                        }
                    }
                }
            }
        }
    }
}
