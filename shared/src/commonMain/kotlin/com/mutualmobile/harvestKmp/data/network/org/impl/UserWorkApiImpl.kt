package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.UserWorkApi
import com.mutualmobile.harvestKmp.domain.model.request.DateRangeWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserWorkApiImpl(private val httpClient: HttpClient) : UserWorkApi {

    override suspend fun getWorkLogsForDateRange(
        startDate: String,
        endDate: String,
        userIds: List<String>?
    ): NetworkResponse<ApiResponse<List<HarvestUserWorkResponse>>> = getSafeNetworkResponse {
        httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOG_WORK}") {
            contentType(ContentType.Application.Json)
            setBody(
                DateRangeWorkRequest(
                    startDate = startDate, endDate = endDate, userIds = userIds
                )
            )
        }
    }

}