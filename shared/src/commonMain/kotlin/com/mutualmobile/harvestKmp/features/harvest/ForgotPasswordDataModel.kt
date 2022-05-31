package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ForgotPasswordDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    fun forgotPassword(email: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch() {
            onDataState(LoadingState)

            when (val response = useCasesComponent.provideForgotPasswordUseCase()(email)) {
                is NetworkResponse.Success -> {
                    praxisCommand(ModalPraxisCommand("Response", response.data.message ?: "Woah!"))
                    onDataState(SuccessState(response.data))
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.throwable))
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