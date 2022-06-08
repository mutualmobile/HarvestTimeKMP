package com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgProjectsUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class FindProjectsInOrgDataModel(var onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

    private val orgProjectsUseCaseComponent = OrgProjectsUseCaseComponent()
    private val findProjectsInOrgUseCase =
        orgProjectsUseCaseComponent.provideFindProjectsInOrgUseCase()

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
        limit: Int?,
        search: String?
    ): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val findUsersInOrgResponse =
                findProjectsInOrgUseCase(
                    orgId = orgId,
                    offset = offset,
                    limit = limit,
                    search
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(findUsersInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(findUsersInOrgResponse.throwable))
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