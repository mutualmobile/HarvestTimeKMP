package com.mutualmobile.harvestKmp.features

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class PraxisSpringBootAuthDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _logoutStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _changePasswordStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)
    private val _fcmTokenStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _logoutStateFlow.value = ErrorState(throwable)
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
            if (value.userId?.isNotBlank() == true) {
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
                    _fcmTokenStateFlow.value = SuccessState(fcmTokenResponse.data)
                }
                is NetworkResponse.Failure -> {
                    _fcmTokenStateFlow.value = ErrorState(fcmTokenResponse.exception)
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