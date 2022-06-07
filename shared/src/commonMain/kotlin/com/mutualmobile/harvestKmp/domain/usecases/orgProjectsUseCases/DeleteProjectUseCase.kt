package com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class DeleteProjectUseCase(private val orgProjectsApi: OrgProjectsApi) {
    suspend operator fun invoke(
        projectId: String
    ): NetworkResponse<ApiResponse<Unit>> {
        return orgProjectsApi.deleteProject(projectId = projectId)
    }
}