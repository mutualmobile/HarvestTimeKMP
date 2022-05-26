package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ResetPasswordUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>> {
        return praxisSpringBootAPI.resetPassword(resetPasswordRequest)
    }
}