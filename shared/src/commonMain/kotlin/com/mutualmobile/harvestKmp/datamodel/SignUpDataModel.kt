package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.SignUpResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SignUpDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _signUpStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _signUpStateFlow.value = ErrorState(throwable)
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

    fun signUp(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _signUpStateFlow.value = LoadingState
            val signUpResponse = useCasesComponent.provideSignUpUseCase()
                .perform(email, password)
            when (signUpResponse) {
                is NetworkResponse.Success -> {
                    _signUpStateFlow.value = SuccessState(signUpResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _signUpStateFlow.value = ErrorState(signUpResponse.exception)
                }
            }
        }
    }

    data class SuccessState(
        val trendingList: SignUpResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}