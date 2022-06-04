package com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases

import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class LogWorkTimeUseCase(private val userProjectApi: UserProjectApi) {
    suspend operator fun invoke(
        id: String? = null,
        projectId: String,
        userId: String,
        workDate: String,
        workHours: Float,
        note: String? = null
    ): NetworkResponse<ApiResponse<Unit>> {
        return userProjectApi.logWorkTime(
            id = id,
            projectId = projectId,
            userId = userId,
            workDate = workDate,
            workHours = workHours,
            note = note
        )
    }
}
