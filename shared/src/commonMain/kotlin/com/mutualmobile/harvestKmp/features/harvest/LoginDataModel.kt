package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class LoginDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val loginUseCase = useCasesComponent.provideLoginUseCase()
    private val getUserUseCase = useCasesComponent.provideGetUserUseCase()
    private val saveSettingsUseCase = useCasesComponent.provideSaveSettingsUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

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
            when (val loginResponse = loginUseCase(email, password)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(loginResponse.data))
                    saveToken(loginResponse)
                    praxisCommand(
                        NavigationPraxisCommand(
                            screen = Routes.Screen.ORG_USER_FETCH,
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
