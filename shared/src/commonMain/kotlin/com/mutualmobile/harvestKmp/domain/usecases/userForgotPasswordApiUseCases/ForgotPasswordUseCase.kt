package com.mutualmobile.harvestKmp.domain.usecases.userForgotPasswordApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.UserForgotPasswordApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ForgotPasswordFormValidator

class ForgotPasswordUseCase(private val userForgotPasswordApi: UserForgotPasswordApi) {
    suspend operator fun invoke(
        email: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        ForgotPasswordFormValidator()(email = email)
        return userForgotPasswordApi.forgotPassword(email = email)
    }
}