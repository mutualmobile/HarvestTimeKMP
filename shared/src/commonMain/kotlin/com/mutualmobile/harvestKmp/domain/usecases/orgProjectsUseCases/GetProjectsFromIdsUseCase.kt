package com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetProjectsFromIdsUseCase(private val orgProjectsApi: OrgProjectsApi) {
    suspend operator fun invoke(
        projectIds: List<String>
    ) : NetworkResponse<ApiResponse<List<OrgProjectResponse>>> {
        return orgProjectsApi.getListOfProjectsForProjectIds(
            projectIds
        )
    }
}