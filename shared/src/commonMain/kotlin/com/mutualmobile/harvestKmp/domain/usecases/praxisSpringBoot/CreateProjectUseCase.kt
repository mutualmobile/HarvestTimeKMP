package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ProjectCrudValidator

class CreateProjectUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>> {
        ProjectCrudValidator()(name, client)
        return praxisSpringBootAPI.createProject(
            name = name,
            client = client,
            isIndefinite = isIndefinite,
            startDate = startDate,
            endDate = endDate
        )
    }
}