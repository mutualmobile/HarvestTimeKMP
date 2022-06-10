package com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.ForgotPasswordApiUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class ResetPasswordDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
    private val forgotPasswordApiUseCaseComponent = ForgotPasswordApiUseCaseComponent()
    private val resetPasswordUseCase =
        forgotPasswordApiUseCaseComponent.provideResetPasswordUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun resetPassword(password: String, token: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            _dataFlow.emit(LoadingState)
            when (val changePasswordResponse =
                resetPasswordUseCase(
                    ResetPasswordRequest(
                        password = password,
                        token = token
                    )
                )) {
                is NetworkResponse.Success<*> -> {
                    if (changePasswordResponse.data is ApiResponse<*>) {
                        praxisCommand(
                            ModalPraxisCommand(
                                "Response",
                                changePasswordResponse.data.message ?: "Woah!"
                            )
                        )
                    }
                    _dataFlow.emit(SuccessState(changePasswordResponse.data))
                    praxisCommand(NavigationPraxisCommand(""))
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(changePasswordResponse.throwable))
                }
                else -> {}
            }
        }
    }
}