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
import org.koin.core.component.KoinComponent

class SignUpDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

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
        confirmPassword: String,
        email: String,
        password: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            onDataState(LoadingState)
            when (val signUpResponse = existingOrgSignUpUseCase(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
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
        confirmPassword: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String,
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
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

    private fun handleFailure(signUpResponse: NetworkResponse.Failure) {
        onDataState(ErrorState(signUpResponse.throwable))
        praxisCommand(
            ModalPraxisCommand(
                "Error",
                signUpResponse.throwable.message ?: "Error"
            )
        )
        println("FAILED, ${signUpResponse.throwable.message}")
    }

    private fun handleSuccessSignup(signUpResponse: NetworkResponse.Success<ApiResponse<HarvestOrganization>>) {
        onDataState(SuccessState(signUpResponse.data))
        signUpResponse.data.data?.let {
            praxisCommand(
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