package com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases

import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetUserAssignedProjectsUseCase(private val userProjectApi: UserProjectApi) {
    suspend operator fun invoke(
        userId: String?
    ): NetworkResponse<ApiResponse<List<OrgProjectResponse>>> {
        return userProjectApi.getUserAssignedProjects(
            userId = userId
        )
    }
}
