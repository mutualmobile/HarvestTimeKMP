package com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ProjectCrudValidator

class CreateProjectUseCase(private val orgProjectsApi: OrgProjectsApi) {
    suspend operator fun invoke(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>> {
        ProjectCrudValidator()(name = name, client = client)
        return orgProjectsApi.createProject(
            name = name,
            client = client,
            isIndefinite = isIndefinite,
            startDate = startDate,
            endDate = endDate
        )
    }
}