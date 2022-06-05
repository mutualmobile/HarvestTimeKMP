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
import org.koin.core.component.KoinComponent

class UserDashboardDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val logoutUseCase = authApiUseCasesComponent.provideLogoutUseCase()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()
    private val getUserUseCase = authApiUseCasesComponent.provideGetUserUseCase()

    override fun activate() {
        if (!userLoggedInUseCase.invoke()) {
            praxisCommand(NavigationPraxisCommand(""))//take to root
        } else {
            fetchUserInternal()
        }
    }

    private fun fetchUserInternal() {
        dataModelScope.launch(exceptionHandler) {
            onDataState(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(getUserResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    onDataState(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
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
            onDataState(LogoutInProgress)
            when (val result = logoutUseCase.invoke()) {
                is Success<*> -> {
                    println("logged out!")
                    onDataState(SuccessState(result.data))
                    praxisCommand(NavigationPraxisCommand(screen = ""))
                }
                is Failure -> {
                    println("logg out failed!")
                    onDataState(ErrorState(result.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            title = "Error",
                            result.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
                is NetworkResponse.Unauthorized -> {
                    praxisCommand(NavigationPraxisCommand(screen = ""))
                }
            }
        }
    }
}

