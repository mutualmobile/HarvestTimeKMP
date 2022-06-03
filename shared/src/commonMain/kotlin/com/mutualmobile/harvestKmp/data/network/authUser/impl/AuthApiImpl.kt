package com.mutualmobile.harvestKmp.data.network.authUser.impl

import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.data.network.getSafeNetworkResponse
import com.mutualmobile.harvestKmp.domain.model.request.ChangePassword
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.request.LoginData
import com.mutualmobile.harvestKmp.domain.model.request.SignUpData
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {

    override suspend fun fcmToken(user: User): NetworkResponse<ApiResponse<LoginResponse>> =
        getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.FCM_TOKEN}") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
        }

    override suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.SIGNUP}") {
                contentType(ContentType.Application.Json)
                setBody(
                    SignUpData(
                        email = email,
                        password = password,
                        orgId = company,
                        role = UserRole.ORG_USER.role,
                        firstName = firstName,
                        lastName = lastName
                    )
                )
            }
        }
    }

    override suspend fun newOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.SIGNUP}") {
                contentType(ContentType.Application.Json)
                setBody(
                    SignUpData(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        harvestOrganization = HarvestOrganization(
                            name = orgName,
                            website = orgWebsite,
                            identifier = orgIdentifier
                        )
                    )
                )
            }
        }
    }

    override suspend fun logout(): NetworkResponse<ApiResponse<String>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGOUT}") {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): LoginResponse {
        return httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.REFRESH_TOKEN}") {
            contentType(ContentType.Application.Json)
            setBody(LoginResponse(refreshToken = refreshToken))
        }.body()
    }

    override suspend fun getUser(): NetworkResponse<GetUserResponse> = getSafeNetworkResponse {
        httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}")
    }

    override suspend fun changePassword(
        password: String,
        oldPassword: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.CHANGE_PASSWORD}") {
            contentType(ContentType.Application.Json)
            setBody(ChangePassword(password = password, oldPass = oldPassword))
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse> {
        return getSafeNetworkResponse(
            networkOperation = {
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGIN}") {
                    contentType(ContentType.Application.Json)
                    setBody(LoginData(email = email, password = password))
                }
            },
            onSuccessOperations = {
                httpClient
                    .plugin(Auth)
                    .providers
                    .filterIsInstance<BearerAuthProvider>()
                    .firstOrNull()?.clearToken()
            }
        )
    }

    override suspend fun updateUser(id: String): User {
        return httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}").body()
    }

}
