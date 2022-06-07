package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.request.CreateProject
import com.mutualmobile.harvestKmp.domain.model.request.UpdateProjectRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrgProjectsApiImpl(private val httpClient: HttpClient) : OrgProjectsApi {

    override suspend fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ORG_PROJECT}") {
            contentType(ContentType.Application.Json)
            setBody(
                CreateProject(
                    name = name,
                    client = client,
                    isIndefinite = isIndefinite,
                    startDate = startDate,
                    endDate = endDate
                )
            )

        }
    }

    override suspend fun findProjectsInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?,
        search: String?,
    ): NetworkResponse<ApiResponse<Pair<Int, List<OrgProjectResponse>>>> = getSafeNetworkResponse {
        httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ORG_PROJECT}") {
            contentType(ContentType.Application.Json)
            parameter("orgId", orgId)
            parameter("offset", offset)
            parameter("limit", limit)
            search?.let {
                parameter("search", search)
            }
        }
    }

    override suspend fun updateProject(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String?,
        isIndefinite: Boolean,
        organizationId: String
    ): NetworkResponse<ApiResponse<Unit>> = getSafeNetworkResponse {
        httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ORG_PROJECT}") {
            contentType(ContentType.Application.Json)
            setBody(
                UpdateProjectRequest(
                    id = id,
                    name = name,
                    client = client,
                    startDate = startDate,
                    endDate = endDate,
                    isIndefinite = isIndefinite,
                    organizationId = organizationId
                )
            )
        }
    }

    override suspend fun deleteProject(projectId: String): NetworkResponse<ApiResponse<Unit>> =
        getSafeNetworkResponse {
            httpClient.delete("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ORG_PROJECT}") {
                contentType(ContentType.Application.Json)
                parameter("projectId", projectId)
            }
        }

    override suspend fun getListOfUsersForAProject(projectId: String): NetworkResponse<ApiResponse<List<GetUserResponse>>> =
        getSafeNetworkResponse {
            httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LIST_USERS_IN_PROJECT}") {
                contentType(ContentType.Application.Json)
                parameter("projectId", projectId)
            }
        }

}
