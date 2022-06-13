package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
        return flow {
            emit(LoadingState)
            when (val changePasswordResponse = changePasswordUseCase(
                password = password,
                oldPassword = oldPassword
            )) {
                is NetworkResponse.Success -> {
                    print("ChangePassword Successful, ${changePasswordResponse.data.message}")
                    emit(SuccessState(changePasswordResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("ChangePassword Failed, ${changePasswordResponse.throwable.message}")
                    emit(ErrorState(changePasswordResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }.catch {
            this.emit(ErrorState(it))
            println(it)
            it.printStackTrace()
            intPraxisCommand.emit(
                ModalPraxisCommand(
                    title = "Error",
                    it.message ?: "An Unknown error has happened"
                )
            )
        }
    }
}