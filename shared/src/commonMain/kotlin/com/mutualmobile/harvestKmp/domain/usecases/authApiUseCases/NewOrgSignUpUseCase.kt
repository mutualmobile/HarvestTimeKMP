package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.SignUpFormValidator

class NewOrgSignUpUseCase(private val authApi: AuthApi) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        SignUpFormValidator()(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            orgName = orgName,
            website = orgWebsite,
            orgIdentifier = orgIdentifier
        )
        return authApi.newOrgSignUp(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            orgName = orgName,
            orgWebsite = orgWebsite,
            orgIdentifier = orgIdentifier
        )
    }
}