package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class LoginDataModel(onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    override fun activate() {
        print("activate called")
        listenState()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun login(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            dataState.value = LoadingState
            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(email, password)
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    print(loginResponse.data)
                    dataState.value =
                        SuccessState(loginResponse.data)
                }
                is NetworkResponse.Failure -> {
                    dataState.value =
                        ErrorState(loginResponse.exception)
                }
            }
        }
    }
}
