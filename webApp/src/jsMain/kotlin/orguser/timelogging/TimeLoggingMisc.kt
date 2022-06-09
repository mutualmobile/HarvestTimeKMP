package orguser.timelogging

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listUsersWithProjectId
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

external interface NewEntryButtonProps : Props {
    var clicked: () -> Unit
}

external interface SaveTimeButtonProps : Props {
    var workId: String?
    var saveProjectId: String?
    var saveuserId: String?
    var date: String
    var note: String
    var workHours: Float
    var dataModel: TimeLogginDataModel
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
                                props.workId,
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
            width = 60.px
            height = 60.px
            backgroundColor = NamedColor.darkgreen
            alignItems = AlignItems.center
            display = Display.flex
            borderRadius = 6.px

        }
        mui.icons.material.Add() {
            sx {
                color = NamedColor.white
                width = 60.px
                height = 60.px
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
    var onEditRequested:(HarvestUserWorkResponse)->Unit
    var onDeleteRequested:(HarvestUserWorkResponse)->Unit

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
                props.workWeek?.filter { format(Date(it.workDate), "yyyy-MM-dd") as String == props.selectDateString }
                    ?.map { work ->
                        val projectName =
                            props.assignedProjects?.firstOrNull { it.id == work.projectId }?.name ?: ""
                        // TODO We don't want to do this, instead get the project name form the api response
                        ListItem{
                            ListItemText {
                                primary = "Project: $projectName ".toReactNode()
                                secondary =
                                    "Time: ${work.workHours} Note: ${work.note.toString()}".toReactNode()
                            }
                            ListItemSecondaryAction{
                                IconButton {
                                    mui.icons.material.Edit()
                                    onClick = {
                                        props.onEditRequested(work)
                                    }
                                }

                                IconButton {
                                    mui.icons.material.Delete()
                                    onClick = {
                                        props.onDeleteRequested(work)
                                    }
                                }
                            }
                        }
                    }
            }
        }


    }
}

private fun String.toReactNode(): ReactNode {
    return ReactNode(this)
}