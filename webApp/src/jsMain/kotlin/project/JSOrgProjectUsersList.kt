package project

import com.mutualmobile.harvestKmp.datamodel.BROWSER_SCREEN_ROUTE_SEPARATOR
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.GetListOfUsersForAProjectDataModel
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mui.material.Box
import mui.material.CircularProgress
import mui.material.ListItem
import mui.material.ListItemText
import mui.material.List
import react.ReactNode
import react.VFC
import react.router.dom.useSearchParams
import react.router.useNavigate
import react.useEffectOnce
import react.useState

val JSOrgProjectUsersList = VFC {

    val searchParams = useSearchParams()
    val projectId: String? = searchParams.component1().get(HarvestRoutes.Keys.id)
    var isLoading by useState(false)
    var message by useState("")
    var users by useState<List<GetUserResponse>>()
    val navigator = useNavigate()

    val dataModel = GetListOfUsersForAProjectDataModel().apply{
        this.dataFlow.onEach { dataState: DataState ->
            isLoading = dataState is LoadingState
            when (dataState) {
                is SuccessState<*> -> {
                    try {
                        val response = (dataState.data as ApiResponse<List<GetUserResponse>>)
                        users = response.data
                        message = "There are ${response.data?.size.toString()} users assigned to this project"
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                is ErrorState -> {
                    message = dataState.throwable.message.toString()
                }
                is LoadingState -> {
                    message = "Loading..."
                }
                else -> {}
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
        dataModel.getListOfUsersForAProject(projectId.toString())
    }


    Box {
        TopAppBar {
            title = "Project users"
            subtitle = message
        }

        if (isLoading) {
            CircularProgress()
        } else {
            Box {
                List {
                    users?.map { user ->
                        ListItem {
                            ListItemText {
                                primary = ReactNode("${user.firstName ?: ""} ${user.lastName ?: ""}")
                                secondary = ReactNode("${user.email}")
                            }
                        }
                    }
                }
            }
        }
    }
}
