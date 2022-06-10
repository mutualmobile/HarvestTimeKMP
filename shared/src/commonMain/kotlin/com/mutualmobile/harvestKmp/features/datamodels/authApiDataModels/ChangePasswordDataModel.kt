package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class ChangePasswordDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val changePasswordUseCase = authApiUseCasesComponent.provideChangePasswordUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun changePassWord(password: String, oldPassword: String): Flow<DataState> {
        return callbackFlow {
            this.send(LoadingState)
            when (val changePasswordResponse = changePasswordUseCase(
                password = password,
                oldPassword = oldPassword
            )) {
                is NetworkResponse.Success -> {
                    print("ChangePassword Successful, ${changePasswordResponse.data.message}")
                    this.send(SuccessState(changePasswordResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("ChangePassword Failed, ${changePasswordResponse.throwable.message}")
                    this.send(ErrorState(changePasswordResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }.catch {
            this.emit(ErrorState(it))
            println(it)
            it.printStackTrace()
            praxisCommand(
                ModalPraxisCommand(
                    title = "Error",
                    it.message ?: "An Unknown error has happened"
                )
            )
        }
    }
}