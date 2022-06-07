package com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.ForgotPasswordApiUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ResetPasswordDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

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
            onDataState(LoadingState)
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
                    onDataState(SuccessState(changePasswordResponse.data))
                    praxisCommand(NavigationPraxisCommand(""))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(changePasswordResponse.throwable))
                }
                else -> {}
            }
        }
    }
}