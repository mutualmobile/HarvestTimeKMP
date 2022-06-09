package com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases

import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class DeleteWorkTimeUseCase(private val userProjectApi: UserProjectApi) {
    suspend operator fun invoke(harvestUserWorkRequest: HarvestUserWorkResponse
    ): NetworkResponse<ApiResponse<Unit>> {
        return userProjectApi.deleteWorkTime(harvestUserWorkRequest
        )
    }
}
