package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse.Failure
import com.mutualmobile.harvestKmp.features.NetworkResponse.Success
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LogoutDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val logoutUseCase = authApiUseCasesComponent.provideLogoutUseCase()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()

    override fun activate() {
        if (!userLoggedInUseCase.invoke()) {
            praxisCommand(NavigationPraxisCommand(""))//take to root
        }
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun logout() {
        dataModelScope.launch(exceptionHandler) {
            onDataState(LoadingState)
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