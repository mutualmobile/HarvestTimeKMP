package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot.userProject

import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class AssignProjectsToUsersUseCase(private val userProjectApi: UserProjectApi) {
    suspend operator fun invoke(
        projectId: String,
        userId: List<String>
    ): NetworkResponse<ApiResponse<Unit>> {
        return userProjectApi.assignProjectsToUsers(
            projectId = projectId,
            userId = userId
        )
    }
}