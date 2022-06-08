package com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgProjectsUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class OrgProjectsDataModel(var onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val orgProjectsUseCaseComponent = OrgProjectsUseCaseComponent()
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
    ): Flow<DataState> {
        return flow {
            emit(LoadingState)
            when (val createProjectResponse = createProjectUseCase(
                name = name,
                client = client,
                isIndefinite = isIndefinite,
                startDate = startDate,
                endDate = endDate
            )) {
                is NetworkResponse.Success -> {
                    emit(SuccessState(createProjectResponse.data))
                    println("SUCCESS ${createProjectResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    emit(ErrorState(createProjectResponse.throwable))
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
    ): Flow<DataState> {
        return flow {
            emit(LoadingState)
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
                    emit(SuccessState(updateProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    emit(ErrorState(updateProjectResponse.throwable))
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
    ): Flow<DataState> {
        return flow {
            emit(LoadingState)
            when (val deleteProjectResponse = deleteProjectUseCase(
                projectId = projectId
            )) {
                is NetworkResponse.Success -> {
                    emit(SuccessState(deleteProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    emit(ErrorState(deleteProjectResponse.throwable))
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
