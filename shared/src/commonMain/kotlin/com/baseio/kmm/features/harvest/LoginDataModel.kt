package com.baseio.kmm.features.harvest

import com.baseio.kmm.datamodel.PraxisDataModel
import com.baseio.kmm.di.SpringBootAuthUseCasesComponent
import com.baseio.kmm.di.UseCasesComponent
import com.baseio.kmm.domain.model.response.LoginResponse
import com.baseio.kmm.features.NetworkResponse
import com.baseio.kmm.features.LoginDataModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginDataModel : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _loginState: MutableStateFlow<LoginDataModel.DataState> = MutableStateFlow(
        LoginDataModel.EmptyState
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _loginState.value = LoginDataModel.ErrorState(throwable)
    }

    fun login(email:String,password:String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _loginState.value = LoginDataModel.LoadingState
            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(email, password)
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    _loginState.value =
                        LoginDataModel.SuccessState(loginResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _loginState.value =
                        LoginDataModel.ErrorState(loginResponse.exception)
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
