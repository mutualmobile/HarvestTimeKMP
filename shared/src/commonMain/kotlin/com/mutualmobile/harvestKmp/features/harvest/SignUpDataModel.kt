package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.Routes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
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
                    onDataState(SuccessState(signUpResponse.data))
                    println("SUCCESS ${signUpResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(signUpResponse.throwable))
                    println("FAILED, ${signUpResponse.throwable.message}")
                }
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
        currentLoadingJob = dataModelScope.launch() {
            onDataState(LoadingState)
            when (val signUpResponse = useCasesComponent.provideNewOrgSignUpUseCase()
                .invoke(firstName, lastName, email, password, orgName, orgWebsite, orgIdentifier)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(signUpResponse.data))
                    praxisCommand(
                        NavigationPraxisCommand(
                            Routes.Screen.LOGIN.withOrgId(
                                signUpResponse.data.data?.identifier,
                                signUpResponse.data.data?.id
                            )
                        )
                    )
                    println("SUCCESS ${signUpResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(signUpResponse.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            "Error",
                            signUpResponse.throwable.message ?: "Error"
                        )
                    )
                    println("FAILED, ${signUpResponse.throwable.message}")
                }
            }
        }
    }

}