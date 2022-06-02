package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.data.network.Endpoint.CHANGE_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.FORGOT_PASSWORD
import com.mutualmobile.harvestKmp.data.network.Endpoint.ORG_PROJECT
import com.mutualmobile.harvestKmp.data.network.Endpoint.ORG_USERS
import com.mutualmobile.harvestKmp.data.network.Endpoint.RESET_PASSWORD_ENDPOINT
import com.mutualmobile.harvestKmp.domain.model.request.ChangePassword
import com.mutualmobile.harvestKmp.domain.model.request.CreateProject
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.request.LoginData
import com.mutualmobile.harvestKmp.domain.model.request.ResetPasswordRequest
import com.mutualmobile.harvestKmp.domain.model.request.SignUpData
import com.mutualmobile.harvestKmp.domain.model.request.UpdateProjectRequest
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient) :
    PraxisSpringBootAPI {

    override suspend fun getUser(): NetworkResponse<GetUserResponse> = getSafeNetworkResponse {
        httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.USER}")
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

    override suspend fun logout(): NetworkResponse<ApiResponse<String>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.LOGOUT}") {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun fcmToken(user: User): NetworkResponse<ApiResponse<LoginResponse>> =
        getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${Endpoint.FCM_TOKEN}") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
        }

    override suspend fun changePassword(
        password: String,
        oldPassword: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$CHANGE_PASSWORD") {
            contentType(ContentType.Application.Json)
            setBody(ChangePassword(password = password, oldPass = oldPassword))
        }
    }

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

    override suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>> =
        getSafeNetworkResponse {
            httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}${FORGOT_PASSWORD}") {
                contentType(ContentType.Application.Json)
                parameter("email", email)
            }
        }

    override suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$RESET_PASSWORD_ENDPOINT") {
            contentType(ContentType.Application.Json)
            setBody(resetPasswordRequest)
        }
    }

    override suspend fun findUsersInOrg(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> =
        getSafeNetworkResponse {
            httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_USERS") {
                contentType(ContentType.Application.Json)
                parameter("userType", userType)
                parameter("orgIdentifier", orgIdentifier)
                parameter("isUserDeleted", isUserDeleted)
                parameter("offset", offset)
                parameter("limit", limit)
            }
        }

    override suspend fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>> = getSafeNetworkResponse {
        httpClient.post("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
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
        limit: Int?
    ): NetworkResponse<ApiResponse<Pair<Int, List<OrgProjectResponse>>>> = getSafeNetworkResponse {
        httpClient.get("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
            contentType(ContentType.Application.Json)
            parameter("orgId", orgId)
            parameter("offset", offset)
            parameter("limit", limit)
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
        httpClient.put("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
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
            httpClient.delete("${Endpoint.SPRING_BOOT_BASE_URL}$ORG_PROJECT") {
                contentType(ContentType.Application.Json)
                parameter("projectId", projectId)
            }
        }
}