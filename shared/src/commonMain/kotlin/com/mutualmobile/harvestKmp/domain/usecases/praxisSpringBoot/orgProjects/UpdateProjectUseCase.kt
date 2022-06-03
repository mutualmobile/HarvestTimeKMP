package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot.orgProjects

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ProjectCrudValidator

class UpdateProjectUseCase(private val orgProjectsApi: OrgProjectsApi) {
    suspend operator fun invoke(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String?,
        isIndefinite: Boolean,
        organizationId: String
    ): NetworkResponse<ApiResponse<Unit>> {
        ProjectCrudValidator()(name, client)
        return orgProjectsApi.updateProject(
            id,
            name,
            client,
            startDate,
            endDate,
            isIndefinite,
            organizationId
        )
    }
}