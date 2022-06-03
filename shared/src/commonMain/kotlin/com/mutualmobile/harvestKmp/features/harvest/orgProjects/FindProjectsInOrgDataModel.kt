package com.mutualmobile.harvestKmp.features.harvest.orgProjects

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgProjectsUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindProjectsInOrgDataModel(var onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = OrgProjectsUseCaseComponent()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun findProjectInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            onDataState(LoadingState)
            when (val findUsersInOrgResponse =
                useCasesComponent.provideFindProjectsInOrgUseCase()(
                    orgId = orgId,
                    offset = offset,
                    limit = limit
                )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(findUsersInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(findUsersInOrgResponse.throwable))
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