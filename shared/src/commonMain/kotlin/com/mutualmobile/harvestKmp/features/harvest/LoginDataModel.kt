package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class LoginDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val loginUseCase = useCasesComponent.provideLoginUseCase()
    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun login(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val loginResponse = loginUseCase.perform(email, password)) {
                is NetworkResponse.Success -> {
                    print(loginResponse.data)
                    onDataState(SuccessState(loginResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(loginResponse.exception))
                }
            }
        }
    }
}
