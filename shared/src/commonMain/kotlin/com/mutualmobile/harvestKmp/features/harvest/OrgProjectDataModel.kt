package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class OrgProjectDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val settings = SharedComponent().provideSettings()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val createProjectResponse = useCasesComponent.provideCreateProjectUseCase()(name, client, isIndefinite, startDate, endDate)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(createProjectResponse.data))
                    println("SUCCESS ${createProjectResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(createProjectResponse.throwable))
                    println("FAILED, ${createProjectResponse.throwable.message}")
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun updateProject(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String?,
        isIndefinite: Boolean,
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val updateProjectResponse = useCasesComponent.provideUpdateProjectUseCase()(
                id, name, client, startDate, endDate, isIndefinite, settings.getString(Constants.ORGANIZATION_ID)
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(updateProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(updateProjectResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun deleteProject(
        projectId: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val deleteProjectResponse = useCasesComponent.provideDeleteProjectUseCase()(
                projectId
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(deleteProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(deleteProjectResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}