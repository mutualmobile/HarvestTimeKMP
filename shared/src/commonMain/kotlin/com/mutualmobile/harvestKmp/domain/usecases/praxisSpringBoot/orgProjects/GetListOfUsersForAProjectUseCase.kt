package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot.orgProjects

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetListOfUsersForAProjectUseCase(private val orgProjectsApi: OrgProjectsApi) {

    suspend operator fun invoke(
        projectId: String
    ) : NetworkResponse<ApiResponse<List<GetUserResponse>>> {
        return orgProjectsApi.getListOfUsersForAProject(
            projectId = projectId
        )
    }

}