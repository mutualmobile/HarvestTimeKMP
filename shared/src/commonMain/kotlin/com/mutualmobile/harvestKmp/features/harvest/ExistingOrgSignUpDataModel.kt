package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ExistingOrgSignUpDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    override fun activate() {
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
        currentLoadingJob = dataModelScope.launch() {
            onDataState(LoadingState)
            val signUpResponse = useCasesComponent.provideExistingOrgSignUpUseCase()
                .perform(firstName, lastName, company, email, password)
            when (signUpResponse) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(signUpResponse.data))
                    println("SUCCESS ${signUpResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(signUpResponse.exception))
                    println("FAILED, ${signUpResponse.exception.message}")
                }
            }
        }
    }

}