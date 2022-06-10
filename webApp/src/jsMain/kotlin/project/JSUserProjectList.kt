package project

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.GetUserAssignedProjectsDataModel
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.material.Box
import mui.material.CircularProgress
import mui.material.ListItem
import mui.material.ListItemText
import react.ReactNode
import react.VFC
import react.router.dom.useSearchParams
import react.router.useNavigate
import react.useEffectOnce
import react.useState

val JSUserProjectList = VFC {

    val searchParams = useSearchParams()
    val userId: String? = searchParams.component1().get(HarvestRoutes.Keys.id)
    var isLoading by useState(false)
    var message by useState("")
    var projects by useState<List<OrgProjectResponse>>()
    val navigator = useNavigate()

    val dataModel = GetUserAssignedProjectsDataModel().apply {
        dataFlow.onEach {  dataState: PraxisDataModel.DataState ->
            isLoading = dataState is PraxisDataModel.LoadingState
            when (dataState) {
                is PraxisDataModel.SuccessState<*> -> {
                    try {
                        val response = (dataState.data as ApiResponse<List<OrgProjectResponse>>)
                        projects = response.data
                        message =
                            "There are ${response.data?.size.toString()} projects assigned to this user"
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                is PraxisDataModel.ErrorState -> {
                    message = dataState.throwable.message.toString()
                }
                is PraxisDataModel.LoadingState -> {
                    message = "Loading..."
                }
                else -> {}
            } }.launchIn(dataModelScope)
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
        dataModel.getUserAssignedProjects(userId.toString())
    }


    Box {
        TopAppBar {
            title = "User Projects"
            subtitle = message
        }

        if (isLoading) {
            CircularProgress()
        } else {
            Box {
                mui.material.List {
                    projects?.map { project ->
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Project Name : ${project.name ?: ""}")
                                secondary = ReactNode("Client : ${project.client}")
                            }
                        }
                    }
                }
            }
        }
    }
}
