package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.SignUpResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SignUpDataModel(onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    override fun activate() {
        listenState()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun signUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            dataState.value = LoadingState
            val signUpResponse = useCasesComponent.provideSignUpUseCase()
                .perform(firstName, lastName, company, email, password)
            when (signUpResponse) {
                is NetworkResponse.Success -> {
                    dataState.value = SuccessState(signUpResponse.data)
                    println("SUCCESS ${signUpResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    dataState.value = ErrorState(signUpResponse.exception)
                    println("FAILED, ${signUpResponse.exception.message}")
                }
            }
        }
    }

}