package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.LogoutInProgress
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse.Failure
import com.mutualmobile.harvestKmp.features.NetworkResponse.Success
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class UserDashboardDataModel(var onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val logoutUseCase = authApiUseCasesComponent.provideLogoutUseCase()
    private val userLoggedInUseCase = useCasesComponent.providerUserLoggedInUseCase()
    private val getUserUseCase = authApiUseCasesComponent.provideGetNetworkUserUseCase()
    private val harvestUserLocal = SharedComponent().provideHarvestUserLocal()

    override fun activate() {
        if (!userLoggedInUseCase.invoke()) {
            praxisCommand(NavigationPraxisCommand(""))//take to root
        } else {
            fetchUserInternal()
        }
    }

    private fun fetchUserInternal(): Flow<DataState> {
        return flow {
            emit(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is Success -> {
                    harvestUserLocal.saveUser(getUserResponse.data)
                    emit(SuccessState(getUserResponse.data))
                }
                is Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    emit(ErrorState(getUserResponse.throwable))
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

    fun logout(): Flow<DataState> {
        return flow {
            emit(LogoutInProgress)
            when (val result = logoutUseCase.invoke()) {
                is Success<*> -> {
                    println("logged out!")
                    emit(SuccessState(result.data))
                    praxisCommand(NavigationPraxisCommand(screen = ""))
                }
                is Failure -> {
                    println("logg out failed!")
                    emit(ErrorState(result.throwable))
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

