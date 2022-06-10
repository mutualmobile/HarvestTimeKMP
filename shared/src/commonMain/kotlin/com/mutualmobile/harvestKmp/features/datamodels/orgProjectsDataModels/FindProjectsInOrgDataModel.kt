package com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgProjectsUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class FindProjectsInOrgDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
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
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LoadingState)
            when (val findUsersInOrgResponse =
                findProjectsInOrgUseCase(
                    orgId = orgId,
                    offset = offset,
                    limit = limit,
                    search
                )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(findUsersInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(findUsersInOrgResponse.throwable))
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