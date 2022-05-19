package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginDataModel : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _loginState: MutableStateFlow<DataState> = MutableStateFlow(
        EmptyState
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _loginState.value = ErrorState(throwable)
    }

    fun login(email:String,password:String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _loginState.value = LoadingState
            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(email, password)
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    _loginState.value =
                        SuccessState(loginResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _loginState.value =
                        ErrorState(loginResponse.exception)
                }
            }
        }
    }


    override fun activate() {

    }

    override fun destroy() {

    }

    override fun refresh() {

    }

    sealed class DataState
    object LoadingState : DataState()
    object EmptyState : DataState()
    object Complete : DataState()
    data class SuccessState(
        val loginResponse: LoginResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}
