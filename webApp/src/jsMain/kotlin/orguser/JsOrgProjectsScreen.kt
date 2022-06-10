package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listUsersWithProjectId
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.FindProjectsInOrgDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.material.*
import mui.icons.material.Add
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.router.useNavigate
import kotlin.js.Date


val JsOrgProjectsScreen = VFC {
    var createRequested by useState(false)
    var selectedProject by useState<OrgProjectResponse?>(null)
    val navigator = useNavigate()
    var projects by useState<List<OrgProjectResponse>>()
    val limit = 20
    var currentPage by useState(0)
    var totalPages by useState(0)
    var isLoading by useState(false)
    var searchProject by useState<String>()


    val dataModel = FindProjectsInOrgDataModel().apply {
        dataFlow.onEach { stateNew: PraxisDataModel.DataState ->
            isLoading = stateNew is PraxisDataModel.LoadingState
            when (stateNew) {
                is PraxisDataModel.SuccessState<*> -> {
                    try {
                        val response =
                            (stateNew.data as ApiResponse<Pair<Int, List<OrgProjectResponse>>>)
                        projects = response.data?.second
                        totalPages = response.data?.first ?: 0
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }else -> {}
            }
        }.launchIn(dataModelScope)
    }

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


    useEffectOnce {
        dataModel.activate()
        dataModel.findProjectInOrg(
            orgId = null, offset = currentPage, limit = limit, search = searchProject
        )
    }

    Box {
        Box {
            sx {
                position = Position.relative
                transform = translatez(0.px)
                flexGrow = number(1.0)
                alignSelf = AlignSelf.flexEnd
                alignItems = AlignItems.baseline
            }


            FormControl {
                InputLabel {
                    +"Search Projects"
                }
                OutlinedInput {
                    placeholder = "Search Projects by name"
                    onChange = {
                        val target = it.target as HTMLInputElement
                        dataModel.findProjectInOrg(
                            offset = currentPage,
                            limit = limit,
                            orgId = null,
                            search = target.value
                        )
                        currentPage = 0
                        searchProject = target.value
                    }
                }
            }

            if (isLoading) {
                CircularProgress()
            } else {
                Pagination {
                    count = totalPages
                    page = currentPage
                    onChange = { event, value ->
                        currentPage = value.toInt()
                        dataModel.findProjectInOrg(
                            orgId = null,
                            offset = value.toInt().minus(1),
                            limit = limit,
                            search = searchProject
                        )
                    }

                }
                List {
                    projects?.map { project ->
                        val format: dynamic = kotlinext.js.require("date-fns").format
                        val start =
                            format(Date(project.startDate.toString()), "yyyy-MM-dd") as String
                        val end =
                            format(Date(project.endDate.toString()), "yyyy-MM-dd") as? String
                        ListItem {
                            ListItemText {
                                primary =
                                    ReactNode("Name: ${project.name ?: ""}\nClient: ${project.client ?: ""}")
                                secondary =
                                    ReactNode("Start Date: $start EndDate: $end")
                            }
                            ListItemSecondaryAction {
                                IconButton {
                                    mui.icons.material.ArrowForwardIos()
                                    onClick = {
                                        navigator.invoke(
                                            BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.LIST_USERS_PROJECT.listUsersWithProjectId(
                                                project.id
                                            )
                                        )
                                    }
                                }
                            }
                            onClick = {
                                selectedProject = project
                                createRequested = true
                            }
                        }
                    }
                }
            }
        }


        JsCreateProject {
            key = selectedProject?.id ?: "1"
            drawerOpen = createRequested
            projectClicked = selectedProject
            onOpen = {
                createRequested = true
            }
            onClose = {
                createRequested = false
                selectedProject = null
                currentPage = 0
                dataModel.findProjectInOrg(
                    orgId = null, offset = 0, limit = limit, search = searchProject
                )
            }
        }
    }

    Fab {
        variant = FabVariant.extended
        color = FabColor.primary
        sx {
            transform = translatez(0.px)
            position = Position.absolute
            bottom = 16.px
            right = 16.px
        }
        color = FabColor.primary
        Add()
        onClick = {
            createRequested = true
        }
        +"Create Project"
    }
}