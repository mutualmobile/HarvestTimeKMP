package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class SignUpDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
    private val authApiUseCaseComponent = AuthApiUseCaseComponent()
    private val existingOrgSignUpUseCase = authApiUseCaseComponent.provideExistingOrgSignUpUseCase()
    private val newOrgSignUpUseCase = authApiUseCaseComponent.provideNewOrgSignUpUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun signUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _dataFlow.emit(LoadingState)
            when (val signUpResponse = existingOrgSignUpUseCase(
                firstName = firstName,
                lastName = lastName,
                company = company,
                email = email,
                password = password
            )) {
                is NetworkResponse.Success -> {
                    handleSuccessSignup(signUpResponse)
                }
                is NetworkResponse.Failure -> {
                    handleFailure(signUpResponse)
                }
                else -> {}
            }
        }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String,
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            _dataFlow.emit(LoadingState)
            when (val signUpResponse = newOrgSignUpUseCase(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                orgName = orgName,
                orgWebsite = orgWebsite,
                orgIdentifier = orgIdentifier
            )) {
                is NetworkResponse.Success -> {
                    handleSuccessSignup(signUpResponse)
                }
                is NetworkResponse.Failure -> {
                    handleFailure(signUpResponse)
                }
                else -> {}
            }
        }
    }

    private suspend fun handleFailure(signUpResponse: NetworkResponse.Failure) {
        _dataFlow.emit(ErrorState(signUpResponse.throwable))
        intPraxisCommand.emit(
            ModalPraxisCommand(
                "Error",
                signUpResponse.throwable.message ?: "Error"
            )
        )
        println("FAILED, ${signUpResponse.throwable.message}")
    }

    private suspend fun handleSuccessSignup(signUpResponse: NetworkResponse.Success<ApiResponse<HarvestOrganization>>) {
        _dataFlow.emit(SuccessState(signUpResponse.data))
        signUpResponse.data.data?.let {
            this.intPraxisCommand.emit(
                NavigationPraxisCommand(
                    HarvestRoutes.Screen.LOGIN.withOrgId(
                        signUpResponse.data.data.identifier,
                        signUpResponse.data.data.id
                    )
                )
            )
        }
        println("SUCCESS ${signUpResponse.data.message}")
    }
}