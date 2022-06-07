package com.mutualmobile.harvestKmp.data.network.authUser.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.data.network.authUser.UserForgotPasswordApi
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserForgotPasswordApiImpl(private val httpClient: HttpClient) : UserForgotPasswordApi {

    override suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.RESET_PASSWORD_ENDPOINT}") {
            contentType(ContentType.Application.Json)
            setBody(resetPasswordRequest)
        }
    }

    override suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>> =
        getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.FORGOT_PASSWORD}") {
                contentType(ContentType.Application.Json)
                parameter("email", email)
            }
        }

}
