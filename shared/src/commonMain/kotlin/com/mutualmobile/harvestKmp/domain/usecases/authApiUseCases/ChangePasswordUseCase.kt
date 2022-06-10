package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ChangePasswordFormValidator

class ChangePasswordUseCase(private val authApi: AuthApi) {
    suspend operator fun invoke(
        password: String,
        oldPassword: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        ChangePasswordFormValidator()(password = password, confirmPassword = oldPassword)
        return authApi.changePassword(password = password, oldPassword = oldPassword)
    }
}