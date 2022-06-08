package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes.Screen.withOrgId
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent

class SignUpDataModel(var onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

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
    ): Flow<DataState> {
        return callbackFlow {
            this.send(LoadingState)
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
    ): Flow<DataState> {
        return callbackFlow {
            this.send(LoadingState)
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

    private fun handleFailure(signUpResponse: NetworkResponse.Failure): Flow<DataState> {
        return callbackFlow {
            this.send(ErrorState(signUpResponse.throwable))
            praxisCommand(
                ModalPraxisCommand(
                    "Error",
                    signUpResponse.throwable.message ?: "Error"
                )
            )
            println("FAILED, ${signUpResponse.throwable.message}")
        }
    }

    private fun handleSuccessSignup(signUpResponse: NetworkResponse.Success<ApiResponse<HarvestOrganization>>): Flow<DataState> {
        return callbackFlow {
            this.send(SuccessState(signUpResponse.data))
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
}