package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.OrgProjectDataModel
import com.mutualmobile.harvestKmp.domain.model.response.CreateProjectResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindProjectsInOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.harvest.CreateProjectDataModel
import com.mutualmobile.harvestKmp.features.harvest.FindProjectsInOrgDataModel
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.js.jso
import mui.material.*
import mui.icons.material.Add
import mui.system.sx
import react.*
import react.router.useNavigate

val JsOrgProjectsScreen = VFC {
    var message by useState("")
    var createRequested by useState(false)
    val navigator = useNavigate()
    var projects by useState<List<FindProjectsInOrgResponse>>()
    val limit = 10
    var currentPage by useState(0)
    var totalPages by useState(0)

    val dataModel = OrgProjectDataModel(onDataState = { stateNew ->
    val dataModel = FindProjectsInOrgDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                val response = (stateNew.data as ApiResponse<OrgProjectResponse>)
                response.data
                message = response.message ?: "Some message"
                message = try {
                    val response =
                        (stateNew.data as ApiResponse<Pair<Int, List<FindProjectsInOrgResponse>>>)
                    projects = response.data?.second
                    totalPages = response.data?.first ?: 0

                    response.message ?: "Some message"
                } catch (ex: Exception) {
                    ex.message ?: ""
                }
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Empty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })

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
            offset = currentPage, limit = limit, orgId = null
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
            Pagination {
                count = totalPages
                page = currentPage
                onChange = { event, value ->
                    currentPage = value.toInt()
                    dataModel.findProjectInOrg(
                        offset = value.toInt().minus(1), limit = limit, orgId = null
                    )
                }

            }
            List {
                projects?.map { project ->
                    ListItem {
                        ListItemText {
                            primary =
                                ReactNode("Name: ${project.name ?: ""} Client: ${project.client ?: ""}")
                            secondary =
                                ReactNode("Start Date: ${project.startDate} EndDate: ${project.endDate}")
                        }
                    }
                }
            }

        }

        Fab {
            variant = FabVariant.extended
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

        JsCreateProject {
            drawerOpen = createRequested
            onOpen = {
                createRequested = true
            }
            onClose = {
                createRequested = false
            }
        }
    }
}