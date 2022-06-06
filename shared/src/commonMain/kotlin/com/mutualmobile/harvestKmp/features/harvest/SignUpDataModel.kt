package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.Routes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.datamodel.Routes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
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
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

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
            onDataState(LoadingState)
            when (val signUpResponse = useCasesComponent.provideExistingOrgSignUpUseCase()(
                firstName,
                lastName,
                company,
                email,
                password
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
        confirmPassword:String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String,
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val signUpResponse = useCasesComponent.provideNewOrgSignUpUseCase()
                .invoke(firstName, lastName, email, password, orgName, orgWebsite, orgIdentifier)) {
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
                    Routes.Screen.LOGIN.withOrgId(
                        signUpResponse.data.data.identifier,
                        signUpResponse.data.data.id
                    )
                )
            )
        }
        println("SUCCESS ${signUpResponse.data.message}")
    }
}