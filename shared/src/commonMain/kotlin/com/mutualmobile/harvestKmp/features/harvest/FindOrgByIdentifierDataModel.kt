package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.Routes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    fun findOrgByIdentifier(identifier: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch() {
            onDataState(LoadingState)

            when (val response = useCasesComponent.provideFindOrgByIdentifier()(identifier)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(response.data)) // TODO redundant
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = Routes.Screen.LOGIN.withOrgId(
                                response.data.data?.identifier,
                                response.data.data?.id
                            )
                        )
                    )
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.throwable))
                    println("FAILED, ${response.throwable.message}")
                }
            }
        }
    }

    override fun activate() {
        if (isUserTokenAvailable()) {
            praxisCommand(
                NavigationPraxisCommand(
                    screen = Routes.Screen.ORG_USER_DASHBOARD
                )
            )
        }
    }

    private fun isUserTokenAvailable() = useCasesComponent.providerUserLoggedInUseCase().invoke()

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

}