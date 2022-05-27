package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class OrgProjectDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val getProjectsUseCase = useCasesComponent.provideGetProjectsInOrgUseCase()
    private val settings = Settings()

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
        endDate: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val createProjectResponse = useCasesComponent.provideCreateProjectUseCase()(
                name,
                client,
                isIndefinite,
                startDate,
                endDate
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(createProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(createProjectResponse.throwable))
                }
            }
        }
    }

    fun updateProject(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String,
        isIndefinite: Boolean,
        organizationId: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val updateProjectResponse = useCasesComponent.provideUpdateProjectUseCase()(
                id, name, client, startDate, endDate, isIndefinite, organizationId
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(updateProjectResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(updateProjectResponse.throwable))
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
            }
        }
    }

    fun getProjectsInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val getProjectsInOrgResponse = getProjectsUseCase(orgId, offset, limit)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(getProjectsInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(getProjectsInOrgResponse.throwable))
                }
            }
        }
    }
}