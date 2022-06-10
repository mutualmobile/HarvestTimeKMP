package com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgProjectsUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class OrgProjectsDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
    private val orgProjectsUseCaseComponent = OrgProjectsUseCaseComponent()
    private val getProjectsUseCase = orgProjectsUseCaseComponent.provideGetProjectsFromIdsUseCase()
    private val createProjectUseCase = orgProjectsUseCaseComponent.provideCreateProjectUseCase()
    private val updateProjectUseCase = orgProjectsUseCaseComponent.provideUpdateProjectUseCase()
    private val deleteProjectUseCase = orgProjectsUseCaseComponent.provideDeleteProjectUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

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
            _dataFlow.emit(LoadingState)
            when (val createProjectResponse = createProjectUseCase(
                name = name,
                client = client,
                isIndefinite = isIndefinite,
                startDate = startDate,
                endDate = endDate
            )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(createProjectResponse.data))
                    println("SUCCESS ${createProjectResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(createProjectResponse.throwable))
                    println("FAILED, ${createProjectResponse.throwable.message}")
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
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
            _dataFlow.emit(LoadingState)
            when (val updateProjectResponse = updateProjectUseCase(
                id = id,
                name = name,
                client = client,
                startDate = startDate,
                endDate = endDate,
                isIndefinite = isIndefinite,
                organizationId = harvestLocal.getUser()?.orgId
                    ?: throw RuntimeException("this should not be null")
            )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(updateProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(updateProjectResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
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
            _dataFlow.emit(LoadingState)
            when (val deleteProjectResponse = deleteProjectUseCase(
                projectId = projectId
            )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(deleteProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(deleteProjectResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun getProjectsForProjectIds(
        projectIds: List<String>
    ): Flow<DataState> = flow {
        emit(LoadingState)
        when(
            val response = getProjectsUseCase(projectIds = projectIds)
        ) {
            is NetworkResponse.Success -> {
                emit(SuccessState(response.data))
            }
            is NetworkResponse.Failure -> {
                emit(ErrorState(response.throwable))
            }
            is NetworkResponse.Unauthorized -> {
                settings.clear()
                praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                praxisCommand(NavigationPraxisCommand(""))
            }
        }
    }.catch {
        emit(ErrorState(it))
        println(it)
        it.printStackTrace()
        praxisCommand(
            ModalPraxisCommand(
                title = "Error",
                it.message ?: "An Unknown error has happened"
            )
        )
    }
}
