package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.CreateProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class CreateProjectUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String
    ): NetworkResponse<ApiResponse<CreateProjectResponse>> {
        return praxisSpringBootAPI.createProject(name, client, isIndefinite, startDate, endDate)
    }
}