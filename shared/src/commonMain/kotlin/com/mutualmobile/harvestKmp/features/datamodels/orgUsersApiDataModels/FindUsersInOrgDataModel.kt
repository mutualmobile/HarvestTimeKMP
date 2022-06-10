package com.mutualmobile.harvestKmp.features.datamodels.orgUsersApiDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgUsersApiUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindUsersInOrgDataModel(var onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val orgUsersApiUseCaseComponent = OrgUsersApiUseCaseComponent()
    private val findUsersByOrgUseCase = orgUsersApiUseCaseComponent.provideFindUsersInOrgUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun findUsers(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int,
        searchName: String?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val findUsersInOrgResponse = findUsersByOrgUseCase(
                userType = userType,
                orgIdentifier = orgIdentifier,
                isUserDeleted = isUserDeleted,
                offset = offset,
                limit = limit, searchName = searchName
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
