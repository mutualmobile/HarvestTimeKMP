package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.request.*
import com.mutualmobile.harvestKmp.domain.model.response.FindOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.domain.model.response.SignUpResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

const val SPRING_BOOT_BASE_URL = "http://mmharvestkmp.us-east-2.elasticbeanstalk.com"
const val API_URL = "/api/v1"

const val GET_USER = "/user"
const val PUT_USER = "/user"
const val REFRESH_TOKEN = "/refreshToken"
const val SIGNUP = "/public/signup"
const val LOGIN = "/public/login"
const val LOGOUT = "/logout"
const val FCM_TOKEN = "/fcmToken"
const val CHANGE_PASSWORD = "/changePassword"
const val FIND_ORGANIZATION_BY_IDENTIFIER = "/public/organization"

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient) : PraxisSpringBootAPI {

    override suspend fun getUser(id: String): User {
        return httpClient.get("$SPRING_BOOT_BASE_URL$API_URL$GET_USER").body()
    }

    override suspend fun putUser(id: String): User {
        return httpClient.put("$SPRING_BOOT_BASE_URL$API_URL$PUT_USER").body()
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): RefreshToken {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$REFRESH_TOKEN").body()
    }

    override suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<SignUpResponse> {
        return try {
            NetworkResponse.Success(httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$SIGNUP") {
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
    ): NetworkResponse<SignUpResponse> {
        return try {
            NetworkResponse.Success(httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$SIGNUP") {
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
            val response = httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGIN") {
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
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGOUT").body()
    }

    override suspend fun fcmToken(): NetworkResponse<LoginResponse> {
        return try {
            NetworkResponse.Success(
                httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$FCM_TOKEN").body()
            )
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun changePassword(
        password: String,
        oldPassword: String
    ): ChangePassword {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$CHANGE_PASSWORD").body()
    }

    override suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<FindOrgResponse> {
        return try {
            NetworkResponse.Success(
                httpClient.get("$SPRING_BOOT_BASE_URL$API_URL$FIND_ORGANIZATION_BY_IDENTIFIER?identifier=$identifier")
                    .body()
            )
        } catch (e: Exception) {
            println(e)
            NetworkResponse.Failure(e)
        }
    }
}