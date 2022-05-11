package com.baseio.kmm.features

import com.baseio.kmm.datamodel.PraxisDataModel
import com.baseio.kmm.di.SpringBootAuthUseCasesComponent
import com.baseio.kmm.domain.model.ChangePassword
import com.baseio.kmm.domain.model.GithubReposItem
import com.baseio.kmm.domain.model.LoginData
import com.baseio.kmm.features.trending.GithubTrendingDataModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class PraxisSpringBootAuthDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _loginStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _logoutStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _signUpStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _changePasswordStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
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
            val value = useCasesComponent.provideLoginUseCase().perform(email, password)
            if (value.email.isNotBlank() && value.password.isNotBlank()){
                _loginStateFlow.value = Success
            }
        }
    }

    fun signUp(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _signUpStateFlow.value = LoadingState
            val value = useCasesComponent.provideSignUpUseCase().perform(email, password)
            if (value.email.isNotBlank() && value.password.isNotBlank()){
                _signUpStateFlow.value = Success
            }
        }
    }

    fun changePassword(password: String, oldPassword: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _changePasswordStateFlow.value = LoadingState
            val value = useCasesComponent.provideChangePasswordUseCase().perform(password, oldPassword)
            if (value.password.isNotBlank() && value.oldPassword.isNotBlank()){
                _changePasswordStateFlow.value = Success
            }
        }
    }

    fun logout(userId: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _logoutStateFlow.value = LoadingState
            val value = useCasesComponent.provideLogoutUseCase().perform(userId)
            if (value.userId.isNotBlank()){
                _logoutStateFlow.value = Success
            }
        }
    }

    sealed class DataState
    object LoadingState : DataState()
    object EmptyState : DataState()
    object Success : DataState()
//    data class SuccessState(
//        val trendingList: List<LoginData>,
//    ) : DataState()
//
    data class ErrorState(var throwable: Throwable) : DataState()
}