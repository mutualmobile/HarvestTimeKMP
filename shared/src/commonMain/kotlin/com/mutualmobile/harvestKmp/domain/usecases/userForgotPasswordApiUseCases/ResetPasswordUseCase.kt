package com.mutualmobile.harvestKmp.domain.usecases.userForgotPasswordApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.UserForgotPasswordApi
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ResetPasswordUseCase(private val userForgotPasswordApi: UserForgotPasswordApi) {
    suspend operator fun invoke(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>> {
        return userForgotPasswordApi.resetPassword(resetPasswordRequest)
    }
}