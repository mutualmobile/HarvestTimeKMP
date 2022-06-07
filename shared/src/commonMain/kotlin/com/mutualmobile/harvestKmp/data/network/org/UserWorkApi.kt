package com.mutualmobile.harvestKmp.data.network.org

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface UserWorkApi {

    suspend fun getWorkLogsForDateRange(
        startDate: String,
        endDate: String,
        userIds: List<String>? = null
    ): NetworkResponse<ApiResponse<List<HarvestUserWorkResponse>>>

}