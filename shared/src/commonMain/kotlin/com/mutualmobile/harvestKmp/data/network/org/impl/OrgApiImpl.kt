package com.mutualmobile.harvestKmp.data.network.org.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.data.network.org.OrgApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrgApiImpl(private val httpClient: HttpClient) : OrgApi{

    override suspend fun findOrgByIdentifier(
        identifier: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> =
        getSafeNetworkResponse {
            httpClient.get(
                urlString = "${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.UN_AUTH_ORGANISATION}"
            ) {
                contentType(ContentType.Application.Json)
                parameter("identifier", identifier)
            }
        }

}