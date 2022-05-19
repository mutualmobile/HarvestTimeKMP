package com.baseio.kmm.features

import com.baseio.kmm.datamodel.PraxisDataModel
import com.baseio.kmm.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.request.LoginData
import com.mutualmobile.harvestKmp.domain.model.request.SignUpData
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class PraxisSpringBootAuthDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    var loginCredentials = MutableStateFlow(LoginData())
    var signUpCredentials = MutableStateFlow(SignUpData())
    private val _loginStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _logoutStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _signUpStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _changePasswordStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _fcmTokenStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)

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

    fun login() {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _loginStateFlow.value = LoadingState
            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(loginCredentials.value.email, loginCredentials.value.password)
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    _loginStateFlow.value = SuccessState(loginResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _loginStateFlow.value = ErrorState(loginResponse.exception)
                }
            }
        }
    }

    fun signUp() {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _signUpStateFlow.value = LoadingState
            val signUpResponse = useCasesComponent.provideSignUpUseCase()
                .perform(signUpCredentials.value.email, signUpCredentials.value.password)
            when (signUpResponse) {
                is NetworkResponse.Success -> {
                    _loginStateFlow.value = SuccessState(signUpResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _loginStateFlow.value = ErrorState(signUpResponse.exception)
                }
            }
        }
    }

    fun changePassword(password: String, oldPassword: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _changePasswordStateFlow.value = LoadingState
            val changePasswordResponse =
                useCasesComponent.provideChangePasswordUseCase().perform(password, oldPassword)
        }
    }

    fun logout(userId: String) {
        dataModelScope.launch {
            useCasesComponent.provideLogoutUseCase().perform(userId)
        }
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _logoutStateFlow.value = LoadingState
            val value = useCasesComponent.provideLogoutUseCase().perform(userId)
            if (value.userId.isNotBlank()) {
                _logoutStateFlow.value = Success
            }
        }
    }

    fun fcmToken(){
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _fcmTokenStateFlow.value = LoadingState
            val fcmTokenResponse = useCasesComponent.provideFcmTokenUseCase()
                .perform()
            when (fcmTokenResponse){
                is NetworkResponse.Success -> {
                    _loginStateFlow.value = SuccessState(fcmTokenResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _loginStateFlow.value = ErrorState(fcmTokenResponse.exception)
                }
            }
        }
    }



    sealed class DataState
    object LoadingState : DataState()
    object EmptyState : DataState()
    object Success : DataState()
    data class SuccessState(
        val trendingList: LoginResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}