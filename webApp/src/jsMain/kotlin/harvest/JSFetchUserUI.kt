package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.GetUserDataModel
import harvest.material.TopAppBar
import kotlinx.browser.window
import mui.material.Box
import mui.material.Button
import mui.material.CircularProgress
import react.VFC
import react.router.useNavigate
import react.useEffectOnce
import react.useState

val JSFetchUserUI = VFC {

    var isLoading by useState(false)
    var message by useState("")
    var failed by useState(false)
    val navigator = useNavigate()

    val dataModel = GetUserDataModel(onDataState = { dataState: DataState ->
        isLoading = dataState is LoadingState

        when (dataState) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {

            }
            is ErrorState -> {
                message = dataState.throwable.message.toString()
            }
            Complete -> {

            }
            EmptyState -> {

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
    }


    Box {
        TopAppBar {
            title = message
        }

        if (isLoading) {
            CircularProgress()
        }

        if (failed) {
            Button {
                +"Failed! Retry ?"
                onClick = {
                    dataModel.getUser()
                }
            }
        }
    }

}