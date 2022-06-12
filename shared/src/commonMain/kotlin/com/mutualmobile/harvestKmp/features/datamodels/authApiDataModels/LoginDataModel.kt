package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginDataModel :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val loginUseCase = authApiUseCasesComponent.provideLoginUseCase()
    private val saveSettingsUseCase = useCasesComponent.provideSaveSettingsUseCase()
    private val provideLogoutUseCase = authApiUseCasesComponent.provideLogoutUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun login(email: String, password: String) {
        dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LoadingState)
            when (val loginResponse = loginUseCase(
                email = email,
                password = password
            )) {
                is NetworkResponse.Success -> {
                    _dataFlow.emit(SuccessState(loginResponse.data))
                    saveToken(loginResponse)
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = HarvestRoutes.Screen.ORG_USER_DASHBOARD,
                            ""
                        )
                    )
                }
                is NetworkResponse.Failure -> {
                    _dataFlow.emit(ErrorState(loginResponse.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            title = "Error",
                            loginResponse.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
                is NetworkResponse.Unauthorized -> {
                    _dataFlow.emit(ErrorState(loginResponse.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            title = "Error",
                            loginResponse.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
            }
        }
    }

    private fun saveToken(
        loginResponse: NetworkResponse.Success<LoginResponse>
    ) {
        loginResponse.data.token?.let { token ->
            loginResponse.data.refreshToken?.let { refreshToken ->
                saveSettingsUseCase(
                    token,
                    refreshToken
                )
            }
        }
    }

    fun logoutUser() {
        praxisCommand(
            ModalPraxisCommand(
                title = "Work in Progress",
                message = "The mobile client app is currently made for organization users only, if you're an Admin or a SuperAdmin, you can click on OK and go to the Harvest Web App which supports Admin sign-in"
            )
        )
        dataModelScope.launch {
            provideLogoutUseCase()
        }
    }
}
