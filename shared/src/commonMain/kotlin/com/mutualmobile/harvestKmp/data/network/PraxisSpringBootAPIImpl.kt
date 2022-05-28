package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.data.network.Endpoint.CHANGE_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.FORGOT_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.ORG_PROJECT
import com.mutualmobile.harvestKmp.data.network.Endpoint.ORG_USERS
import com.mutualmobile.harvestKmp.data.network.Endpoint.RESET_PASSWORD_ENDPOINT
import com.mutualmobile.harvestKmp.domain.model.request.*
import com.mutualmobile.harvestKmp.domain.model.response.*
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient) :
    PraxisSpringBootAPI {

    override suspend fun getUser(): NetworkResponse<ApiResponse<GetUserResponse>> {
        return try {
            val response = httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}")
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
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
    ): LoginResponse {
        return httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.REFRESH_TOKEN}") {
            contentType(ContentType.Application.Json)
            setBody(LoginResponse(refreshToken = refreshToken))
        }.body()
    }

    override suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            val response =
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.SIGNUP}") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        SignUpData(
                            email = email,
                            password = password,
                            orgId = company,
                            role = "2",//todo extract const
                            firstName = firstName,
                            lastName = lastName
                        )
                    )
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
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
        orgIdentifier: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            val response =
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
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
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
            val response =
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGIN}") {
                    contentType(ContentType.Application.Json)
                    setBody(LoginData(email = email, password = password))
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success<LoginResponse>(response.body()).also {
                        httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
                            .firstOrNull()?.clearToken()
                    }
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun logout(): NetworkResponse<ApiResponse<String>> {
        return try {
            val response = httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGOUT}") {
                contentType(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }

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

        return try {
            val response =
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$CHANGE_PASSWORD") {
                    contentType(ContentType.Application.Json)
                    setBody(ChangePassword(password = password, oldPass = oldPassword))
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            val response =
                httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.UN_AUTH_ORGANISATION}") {
                    contentType(ContentType.Application.Json)
                    parameter("identifier", identifier)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return try {
            val response =
                httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${FORGOT_PASSWORD}") {
                    contentType(ContentType.Application.Json)
                    parameter("email", email)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
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
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun findUsersInOrg(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> {

        return try {
            val response =
                httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_USERS") {
                    contentType(ContentType.Application.Json)
                    parameter("userType", userType)
                    parameter("orgIdentifier", orgIdentifier)
                    parameter("isUserDeleted", isUserDeleted)
                    parameter("offset", offset)
                    parameter("limit", limit)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
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
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>> {
        return try {
            val response = httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
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
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun findProjectsInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ): NetworkResponse<ApiResponse<Pair<Int, List<OrgProjectResponse>>>> {
        return try {
            val response =
                httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
                    contentType(ContentType.Application.Json)
                    parameter("orgId", orgId)
                    parameter("offset", offset)
                    parameter("limit", limit)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResponse.Failure(e)
        }
    }

    override suspend fun updateProject(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String,
        isIndefinite: Boolean,
        organizationId: String
    ): NetworkResponse<ApiResponse<Unit>> {
        return try {
            val response = httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
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
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun deleteProject(projectId: String): NetworkResponse<ApiResponse<Unit>> {
        return try {
            val response = httpClient.delete("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
                contentType(ContentType.Application.Json)
                parameter("projectId", projectId)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    NetworkResponse.Success(response.body())
                }
                HttpStatusCode.Unauthorized -> {
                    NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
                }
                else -> {
                    val responseMsg = response.body<ApiResponse<Unit>>().message
                    NetworkResponse.Failure(Exception(responseMsg))
                }
            }
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }
}