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

class GetListOfUsersForAProjectDataModel(var onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

    private val orgProjectsUseCaseComponent = OrgProjectsUseCaseComponent()
    private val getListOfUsersForAProjectUseCase =
        orgProjectsUseCaseComponent.provideGetListOfUsersForAProjectUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun getListOfUsersForAProject(
        projectId: String
    ): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val response =
                getListOfUsersForAProjectUseCase(
                    projectId = projectId
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(response.data))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
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
