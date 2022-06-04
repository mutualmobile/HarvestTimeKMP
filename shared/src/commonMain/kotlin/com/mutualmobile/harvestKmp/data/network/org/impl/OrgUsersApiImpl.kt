package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.OrgUsersApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrgUsersApiImpl(private val httpClient: HttpClient) : OrgUsersApi {

    override suspend fun findUsersInOrg(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int,search:String?
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> =
        getSafeNetworkResponse {
            httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.ORG_USERS}") {
                contentType(ContentType.Application.Json)
                parameter("userType", userType)
                parameter("orgIdentifier", orgIdentifier)
                parameter("isUserDeleted", isUserDeleted)
                parameter("offset", offset)
                parameter("limit", limit)
                search?.let{
                    parameter("search", search)
                }
            }
        }

}