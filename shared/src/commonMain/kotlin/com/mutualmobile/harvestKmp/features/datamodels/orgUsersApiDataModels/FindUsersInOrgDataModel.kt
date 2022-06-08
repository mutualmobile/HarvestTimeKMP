package com.mutualmobile.harvestKmp.features.datamodels.orgUsersApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.OrgUsersApiUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class FindUsersInOrgDataModel(var onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

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
    ): Flow<DataState> {
        return flow {
            emit(LoadingState)
            when (val findUsersInOrgResponse = findUsersByOrgUseCase(
                userType = userType,
                orgIdentifier = orgIdentifier,
                isUserDeleted = isUserDeleted,
                offset = offset,
                limit = limit, searchName = searchName
            )) {
                is NetworkResponse.Success -> {
                    emit(SuccessState(findUsersInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    emit(ErrorState(findUsersInOrgResponse.throwable))
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
