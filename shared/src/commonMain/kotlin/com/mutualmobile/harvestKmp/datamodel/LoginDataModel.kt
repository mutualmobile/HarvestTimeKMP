package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _loginStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _loginStateFlow.value = ErrorState(throwable)
    }

    override fun activate() {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    fun login(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _loginStateFlow.value = LoadingState

            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(email, password)
            println("LOGIN")
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    _loginStateFlow.value = SuccessState(loginResponse.data)
                    println(loginResponse.data.message + "TAP")
                }
                is NetworkResponse.Failure -> {
                    _loginStateFlow.value = ErrorState(loginResponse.exception)
                    println("FAILED")
                }
            }
        }
    }

    data class SuccessState(
        val trendingList: LoginResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}