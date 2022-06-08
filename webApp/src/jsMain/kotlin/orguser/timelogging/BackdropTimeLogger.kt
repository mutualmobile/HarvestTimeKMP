package orguser.timelogging

import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import csstype.Display
import csstype.FlexDirection
import csstype.number
import csstype.px
import kotlinx.datetime.internal.JSJoda.LocalDate
import mui.icons.material.Check
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.onChange
import kotlin.js.Date


external interface BackdropTimeLoggerProps : Props {
    var closeDialog :()->Unit
    var projectIdNew: (id: String?) -> Unit
    var onNote: (note: String) -> Unit
    var onWorkHours: (workHours: Float) -> Unit
    var dataModel: TimeLogginDataModel

    var projectId: String?
    var userId: String?
    var projects: List<OrgProjectResponse>?
    var selectedDate: LocalDate
    var note: String
    var showTimeLogDialog: Boolean
    var workHours: Float
}

val BackdropTimeLogger = FC<BackdropTimeLoggerProps> { props ->
    Backdrop {
        open = props.showTimeLogDialog

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
                +"New time entry for ${format(Date(props.selectedDate.toString()), "MMMM d LLLL")}"
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

            props.projects?.let {
                MenuList {
                    props.projects?.map { project ->
                        MenuItem {
                            if (project.id == props.projectId) {
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
                                props.projectIdNew(project.id)
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
                value = props.note
                onChange = {
                    val target = it.target as HTMLInputElement
                    props.onNote(target.value)
                }
            }

            TextField {
                sx {
                    padding = 4.px
                }
                placeholder = "0.00"
                type = InputType.number
                variant = FormControlVariant.outlined
                value = props.workHours
                onChange = {
                    val target = it.target as HTMLInputElement
                    props.onWorkHours(target.value.toFloat())
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
                        props.closeDialog()
                    }
                    saveProjectId = props.projectId
                    saveuserId = props.userId
                    this.dataModel = props.dataModel
                    date = format(
                        Date(props.selectedDate.toString()),
                        "yyyy-MM-dd"
                    ) as String
                    this.note = props.note
                    this.workHours = props.workHours
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
                        props.closeDialog()
                    }
                }
            }


        }
    }
}