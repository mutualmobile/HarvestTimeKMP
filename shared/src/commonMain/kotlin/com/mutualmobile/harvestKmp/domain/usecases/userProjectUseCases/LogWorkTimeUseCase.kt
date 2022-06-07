package com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases

import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class LogWorkTimeUseCase(private val userProjectApi: UserProjectApi) {
    suspend operator fun invoke(harvestUserWorkRequest: HarvestUserWorkRequest
    ): NetworkResponse<ApiResponse<Unit>> {
        return userProjectApi.logWorkTime(harvestUserWorkRequest
        )
    }
}
