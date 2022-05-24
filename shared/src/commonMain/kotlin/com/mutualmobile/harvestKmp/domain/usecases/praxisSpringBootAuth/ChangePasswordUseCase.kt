package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ChangePasswordUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(password: String, oldPassword: String): NetworkResponse<ApiResponse<HarvestOrganization>>  {
        return praxisSpringBootAPI.changePassword(password, oldPassword)
    }
}