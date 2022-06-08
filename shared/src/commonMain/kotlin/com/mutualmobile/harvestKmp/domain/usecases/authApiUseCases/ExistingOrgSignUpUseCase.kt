package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.SignUpFormValidator

class ExistingOrgSignUpUseCase(private val authApi: AuthApi) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        confirmPassword: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        SignUpFormValidator()(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )
        return authApi.existingOrgSignUp(
            firstName = firstName,
            lastName = lastName,
            confirmPassword = confirmPassword,
            email = email,
            password = password
        )
    }
}