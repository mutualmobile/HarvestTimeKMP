package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse.Failure
import com.mutualmobile.harvestKmp.features.NetworkResponse.Success
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class UserDashboardDataModel() :
    PraxisDataModel(), KoinComponent {
    private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val logoutUseCase = authApiUseCasesComponent.provideLogoutUseCase()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()
    private val getUserUseCase = authApiUseCasesComponent.provideGetNetworkUserUseCase()
    private val harvestUserLocal = SharedComponent().provideHarvestUserLocal()

    override fun activate() {
        if (!userLoggedInUseCase.invoke()) {
            dataModelScope.launch { intPraxisCommand.emit(NavigationPraxisCommand("")) }//take to root
        } else {
            fetchUserInternal()
        }
    }

    private fun fetchUserInternal() {
        dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    harvestUserLocal.saveUser(getUserResponse.data)
                    _dataFlow.emit(SuccessState(getUserResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    _dataFlow.emit(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun logout() {
        dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LogoutInProgress)
            when (val result = logoutUseCase.invoke()) {
                is Success<*> -> {
                    println("logged out!")
                    _dataFlow.emit(SuccessState(result.data))
                    intPraxisCommand.emit(NavigationPraxisCommand(screen = ""))
                }
                is Failure -> {
                    println("logg out failed!")
                    _dataFlow.emit(ErrorState(result.throwable))
                    intPraxisCommand.emit(
                        ModalPraxisCommand(
                            title = "Error",
                            result.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
                is NetworkResponse.Unauthorized -> {
                    intPraxisCommand.emit(NavigationPraxisCommand(screen = ""))
                }
            }
        }
    }
}

