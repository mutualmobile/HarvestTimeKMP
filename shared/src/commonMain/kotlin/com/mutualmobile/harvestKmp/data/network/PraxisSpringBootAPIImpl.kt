package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.data.network.Endpoint.CHANGE_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.CREATE_PROJECT
import com.mutualmobile.harvestKmp.data.network.Endpoint.FORGOT_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.RESET_PASSWORD_ENDPOINT
import com.mutualmobile.harvestKmp.domain.model.request.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.CreateProjectResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient, private val settings: Settings) : PraxisSpringBootAPI {

    //TODO - can we have a global way for defining this header ? like an interceptor ?
    override suspend fun getUser(): NetworkResponse<ApiResponse<GetUserResponse>>{
        val jwtToken = settings.getString(key = Constants.JWT_TOKEN)
        return try {
            val response = httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}") {
                header("Authorization", "Bearer $jwtToken")
            }
            val responseBody = response.body<ApiResponse<GetUserResponse>>()
            NetworkResponse.Success(responseBody)
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun putUser(id: String): User {
        return httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}").body()
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): RefreshToken {
        return httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.REFRESH_TOKEN}").body()
    }

    override suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            NetworkResponse.Success(httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.SIGNUP}") {
                contentType(ContentType.Application.Json)
                setBody(
                    SignUpData(
                        email = email,
                        password = password,
                        orgId = company,
                        firstName = firstName,
                        lastName = lastName
                    )
                )
            }.body())
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun newOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            NetworkResponse.Success(httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.SIGNUP}") {
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
            }.body())
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse> {
        return try {
            val response = httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGIN}") {
                contentType(ContentType.Application.Json)
                setBody(LoginData(email = email, password = password))
            }
            val responseBody = response.body<LoginResponse>()
            NetworkResponse.Success(responseBody)
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun logout(userId: String): LogoutData {
        return httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGOUT}").body()
    }

    override suspend fun fcmToken(): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            NetworkResponse.Success(
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.FCM_TOKEN}").body()
            )
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun changePassword(
        password: String,
        oldPassword: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        val jwtToken = settings.getString(key = Constants.JWT_TOKEN)
        return try {
            val response = httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$CHANGE_PASSWORD") {
                contentType(ContentType.Application.Json)
                setBody(ChangePassword(password = password, oldPass = oldPassword))
                header("Authorization", "Bearer $jwtToken")
            }
            val responseBody = response.body<ApiResponse<HarvestOrganization>>()
            NetworkResponse.Success(responseBody)
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            NetworkResponse.Success(
                httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.UN_AUTH_ORGANISATION}?identifier=$identifier")
                    .body()
            )
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            NetworkResponse.Success(
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${FORGOT_PASSWORD}/?email=$email")
                    .body()
            )
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>> {
        return try {
            val response =
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$RESET_PASSWORD_ENDPOINT") {
                    contentType(ContentType.Application.Json)
                    setBody(resetPasswordRequest)
                }
            val responseBody = response.body<ApiResponse<Unit>>()
            NetworkResponse.Success(responseBody)
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String
    ): NetworkResponse<ApiResponse<CreateProjectResponse>> {
        val jwtToken = settings.getString(key = Constants.JWT_TOKEN)
        return try {
            val response = httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$CREATE_PROJECT") {
                contentType(ContentType.Application.Json)
                setBody(CreateProject(name, client, isIndefinite, startDate, endDate))
                header("Authorization", "Bearer $jwtToken")
            }
            val responseBody = response.body<ApiResponse<CreateProjectResponse>>()
            NetworkResponse.Success(responseBody)
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }
}