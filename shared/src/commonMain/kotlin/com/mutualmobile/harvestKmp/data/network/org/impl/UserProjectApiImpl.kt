package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.request.SignUpData
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserProjectApiImpl(private val httpClient: HttpClient) : UserProjectApi {

    override suspend fun assignProjectsToUsers(
        projectMap:HashMap<String,List<String>>
    ): NetworkResponse<ApiResponse<Unit>> {
        return getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ASSIGN_PROJECT}") {
                contentType(ContentType.Application.Json)
                setBody(projectMap)
            }
        }
    }

}