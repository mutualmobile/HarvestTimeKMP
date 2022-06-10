package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserProjectApiImpl(private val httpClient: HttpClient) : UserProjectApi {

    override suspend fun assignProjectsToUsers(
        projectMap: HashMap<String, List<String>>
    ): NetworkResponse<ApiResponse<Unit>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ASSIGN_PROJECT}") {
            contentType(ContentType.Application.Json)
            setBody(projectMap)
        }
    }

    override suspend fun deleteWorkTime(harvestUserWorkRequest: HarvestUserWorkResponse): NetworkResponse<ApiResponse<Unit>> =
        getSafeNetworkResponse {
            httpClient.delete("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOG_WORK}") {
                contentType(ContentType.Application.Json)
                setBody(
                    harvestUserWorkRequest
                )
            }
        }

    override suspend fun logWorkTime(
        harvestUserWorkRequest: HarvestUserWorkRequest
    ): NetworkResponse<ApiResponse<Unit>> = getSafeNetworkResponse {
        harvestUserWorkRequest.id?.let {
            httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOG_WORK}") {
                contentType(ContentType.Application.Json)
                setBody(
                    harvestUserWorkRequest
                )
            }
        } ?: run {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOG_WORK}") {
                contentType(ContentType.Application.Json)
                setBody(
                    harvestUserWorkRequest
                )
            }
        }

    }

    override suspend fun getUserAssignedProjects(userId: String?): NetworkResponse<ApiResponse<List<OrgProjectResponse>>> =
        getSafeNetworkResponse {
            httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER_ASSIGNED_PROJECTS}") {
                contentType(ContentType.Application.Json)
                parameter("userId", userId)
            }
        }
}


