package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val useCasesComponent = UseCasesComponent()
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val loginUseCase = authApiUseCasesComponent.provideLoginUseCase()
    private val saveSettingsUseCase = useCasesComponent.provideSaveSettingsUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun login(email: String, password: String) {
        dataModelScope.launch(exceptionHandler) {
            onDataState(LoadingState)
            when (val loginResponse = loginUseCase(
                email = email,
                password = password
            )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(loginResponse.data))
                    saveToken(loginResponse)
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = HarvestRoutes.Screen.ORG_USER_DASHBOARD,
                            ""
                        )
                    )
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(loginResponse.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            title = "Error",
                            loginResponse.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
                is NetworkResponse.Unauthorized -> {
                    onDataState(ErrorState(loginResponse.throwable))
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
}