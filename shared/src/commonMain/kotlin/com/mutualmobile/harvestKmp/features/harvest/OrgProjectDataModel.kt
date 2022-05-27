package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class OrgProjectDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

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

    fun getProjectsInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val getProjectsInOrgResponse =
                useCasesComponent.provideGetProjectsInOrgUseCase()(orgId, offset, limit)) {
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