package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listProjectsAssignedToUser
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.listUsersWithProjectId
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.FindProjectsInOrgDataModel
import csstype.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.css.Display
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mainScope
import mui.material.*
import mui.icons.material.Add
import mui.icons.material.Edit
import mui.icons.material.KeyboardArrowRight
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import orguser.timelogging.format
import react.*
import react.dom.html.TdAlign
import react.router.useNavigate
import kotlin.js.Date


val JsOrgProjectsScreen = VFC {
    var createRequested by useState(false)
    var selectedProject by useState<OrgProjectResponse?>(null)
    val navigator = useNavigate()
    var projects by useState<List<OrgProjectResponse>>()
    var limit by useState(10)
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
                        //totalPages = response.data?.first ?: 0
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                else -> {}
            }
        }.launchIn(dataModelScope)
        praxisCommand.onEach { newCommand ->
            when (newCommand) {
                is NavigationPraxisCommand -> {
                    navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
                }
                is ModalPraxisCommand -> {
                    window.alert(newCommand.title + "\n" + newCommand.message)
                }
            }
        }.launchIn(dataModelScope)
    }



    useEffect(currentPage, limit, searchProject) {
        dataModel.findProjectInOrg(
            offset = currentPage,
            limit = limit,
            orgId = null,
            search = searchProject
        )
    }


    useEffectOnce {
        dataModel.activate()
        dataModel.findProjectInOrg(
            orgId = null, offset = 0, limit = limit, search = searchProject
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
                sx {
                    margin = 4.px
                }
                InputLabel {
                    +"Search Projects"
                }
                OutlinedInput {
                    placeholder = "Search Projects by name"
                    onChange = {
                        val target = it.target as HTMLInputElement
                        searchProject = target.value
                        currentPage = 0
                    }
                }
            }

            if (isLoading) {
                CircularProgress()
            } else {
                TableContainer {
                    component = Paper
                    Table {
                        stickyHeader = true
                        sx {
                            minWidth = 450.px
                        }
                        TableHead {
                            TableRow {

                                TableCell {
                                    align = TdAlign.left
                                    +"Name"
                                }
                                TableCell {
                                    align = TdAlign.left
                                    +"Client"
                                }
                                TableCell {
                                    align = TdAlign.left
                                    +"Start Date"
                                }
                                TableCell {
                                    align = TdAlign.left
                                    +"End Date"
                                }
                                TableCell {
                                    align = TdAlign.center
                                    +"Options"
                                }
                            }
                        }
                        TableBody {
                            projects?.map { project ->

                                TableRow {

                                    key = project.id

                                    TableCell {
                                        align = TdAlign.left
                                        +"${project.name}"
                                    }
                                    TableCell {
                                        align = TdAlign.left
                                        +"${project.client}"
                                    }
                                    TableCell {
                                        align = TdAlign.left
                                        +(format(
                                            Date(project.startDate.toString()),
                                            "yyyy-MM-dd"
                                        ) as String)
                                    }
                                    TableCell {
                                        align = TdAlign.left
                                        +(format(
                                            Date(project.endDate.toString()),
                                            "yyyy-MM-dd"
                                        ) as String)
                                    }
                                    TableCell {
                                        align = TdAlign.left
                                        IconButton {
                                            Edit {

                                            }
                                            onClick = {
                                                selectedProject = project
                                                createRequested = true
                                            }
                                        }
                                    }
                                    TableCell {
                                        align = TdAlign.left
                                        IconButton {
                                            KeyboardArrowRight {

                                            }
                                            onClick = {
                                                navigator.invoke(
                                                    BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.LIST_USERS_PROJECT.listUsersWithProjectId(
                                                        project.id
                                                    )
                                                )
                                            }
                                        }
                                    }



                                    }

                            }
                        }
                    }

                }
                TablePagination {
                    count =
                        -1 //  the next and previous buttons were disabled regardless of count = totalPages being specified
                    page = currentPage
                    rowsPerPage = limit
                    onRowsPerPageChange = { event ->
                        val value =
                            Json.decodeFromString<ValueTarget>(
                                JSON.stringify(event.target).replace("\\", "")
                            ) // dirty fix event.target.value not accessible
                        limit = (value.value)
                        currentPage = (0)
                    }
                    onPageChange = { _, value ->
                        currentPage = (value.toInt())
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