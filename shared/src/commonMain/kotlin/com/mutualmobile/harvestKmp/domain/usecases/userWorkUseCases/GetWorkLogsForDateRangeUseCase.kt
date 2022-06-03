package com.mutualmobile.harvestKmp.domain.usecases.userWorkUseCases

import com.mutualmobile.harvestKmp.data.network.org.UserWorkApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetWorkLogsForDateRangeUseCase(private val userWorkApi: UserWorkApi) {

    suspend operator fun invoke(
        startDate: String,
        endDate: String,
        userIds: List<String>?
    ): NetworkResponse<ApiResponse<List<HarvestUserWorkResponse>>> {
        return userWorkApi.getWorkLogsForDateRange(
            startDate = startDate,
            endDate = endDate,
            userIds = userIds
        )
    }
}
