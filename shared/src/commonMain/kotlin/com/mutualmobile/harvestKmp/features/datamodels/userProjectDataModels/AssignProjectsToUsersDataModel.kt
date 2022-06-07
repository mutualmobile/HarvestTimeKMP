package com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.UserProjectUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AssignProjectsToUsersDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val userProjectUseCaseComponent = UserProjectUseCaseComponent()
    private val assignProjectsToUsersUseCase =
        userProjectUseCaseComponent.provideAssignProjectsToUsersUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun assignProjectsToUsers(
        projectMap: HashMap<String, List<String>>
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val response =
                assignProjectsToUsersUseCase(
                    projectMap = projectMap
                )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(response.data))
                    praxisCommand(
                        ModalPraxisCommand(
                            "Message",
                            response.data.message ?: "Success!"
                        )
                    )
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            "Message",
                            response.throwable.message ?: "Failed!"
                        )
                    )
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}