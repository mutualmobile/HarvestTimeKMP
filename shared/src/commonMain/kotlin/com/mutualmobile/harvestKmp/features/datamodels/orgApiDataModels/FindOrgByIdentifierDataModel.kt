package com.mutualmobile.harvestKmp.features.datamodels.orgApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.OrgApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = UseCasesComponent()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()
    private val orgApiUseCasesComponent = OrgApiUseCaseComponent()
    private val findOrgByIdentifierUseCase = orgApiUseCasesComponent.provideFindOrgByIdentifier()

    fun findOrgByIdentifier(identifier: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)

            when (val response = findOrgByIdentifierUseCase(
                identifier = identifier
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(response.data)) // TODO redundant
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = HarvestRoutes.Screen.LOGIN.withOrgId(
                                response.data.data?.identifier,
                                response.data.data?.id
                            )
                        )
                    )
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            "Failed",
                            response.throwable.message ?: "Failed to find workspace"
                        )
                    )
                    println("FAILED, ${response.throwable.message}")
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }

    override fun activate() {
        if (isUserTokenAvailable()) {
            praxisCommand(
                NavigationPraxisCommand(
                    screen = HarvestRoutes.Screen.ORG_USER_DASHBOARD
                )
            )
        }
    }

    private fun isUserTokenAvailable() = userLoggedInUseCase.invoke()

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

}