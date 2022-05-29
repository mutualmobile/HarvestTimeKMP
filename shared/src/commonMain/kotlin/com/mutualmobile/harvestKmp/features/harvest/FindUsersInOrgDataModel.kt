package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindUsersInOrgDataModel(private val onDataState: (DataState) -> Unit) :
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

    fun findUsers(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val findUsersInOrgResponse = useCasesComponent.provideFindUsersByOrgUseCase()(userType, orgIdentifier, isUserDeleted, offset, limit)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(findUsersInOrgResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(findUsersInOrgResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}
