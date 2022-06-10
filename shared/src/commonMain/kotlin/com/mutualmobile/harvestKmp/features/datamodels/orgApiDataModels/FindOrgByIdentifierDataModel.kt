package com.mutualmobile.harvestKmp.features.datamodels.orgApiDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.OrgApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel() :
    PraxisDataModel(), KoinComponent {
    private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private val useCasesComponent = UseCasesComponent()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()
    private val orgApiUseCasesComponent = OrgApiUseCaseComponent()
    private val findOrgByIdentifierUseCase = orgApiUseCasesComponent.provideFindOrgByIdentifier()

    suspend fun doSomething(): Int {
        return 1
    }

    fun findOrgByIdentifier(identifier: String) {
        dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LoadingState)

            when (val response = findOrgByIdentifierUseCase(
                identifier = identifier
            )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(response.data)) // TODO redundant
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = HarvestRoutes.Screen.LOGIN.withOrgId(
                                response.data.data?.identifier,
                                response.data.data?.id
                            )
                        )
                    )
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(response.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            "Failed",
                            response.throwable.message ?: "Failed to find workspace"
                        )
                    )
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