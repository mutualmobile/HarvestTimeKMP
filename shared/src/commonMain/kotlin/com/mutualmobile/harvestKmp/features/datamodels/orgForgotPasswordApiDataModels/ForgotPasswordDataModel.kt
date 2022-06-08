package com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.ForgotPasswordApiUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class ForgotPasswordDataModel(var onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val forgotPasswordApiUseCaseComponent = ForgotPasswordApiUseCaseComponent()
    private val forgotPasswordUseCase =
        forgotPasswordApiUseCaseComponent.provideForgotPasswordUseCase()

    fun forgotPassword(email: String): Flow<DataState> {
        return flow {
            emit(LoadingState)

            when (val response = forgotPasswordUseCase(
                email = email
            )) {
                is NetworkResponse.Success -> {
                    praxisCommand(ModalPraxisCommand("Response", response.data.message ?: "Woah!"))
                    emit(SuccessState(response.data))
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    emit(ErrorState(response.throwable))
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
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }
}