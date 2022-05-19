package com.baseio.kmm.data.network

import com.mutualmobile.harvestKmp.domain.model.request.ChangePassword
import com.mutualmobile.harvestKmp.domain.model.request.LogoutData
import com.mutualmobile.harvestKmp.domain.model.request.RefreshToken
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.*
import io.ktor.client.request.*

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

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient) : PraxisSpringBootAPI {

    override suspend fun getUser(id: String): User {
        return httpClient.get("$SPRING_BOOT_BASE_URL$API_URL$GET_USER")
    }

    override suspend fun putUser(id: String): User {
        return httpClient.put("$SPRING_BOOT_BASE_URL$API_URL$PUT_USER")
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): RefreshToken {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$REFRESH_TOKEN")
    }

    override suspend fun signup(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse> {
        return try {
            NetworkResponse.Success(httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$SIGNUP"))
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
            NetworkResponse.Success(httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGIN"))
        } catch (e: Exception){
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun logout(userId: String): LogoutData {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGOUT")
    }

    override suspend fun fcmToken(): NetworkResponse<LoginResponse> {
        return try {
            NetworkResponse.Success(httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$FCM_TOKEN"))
        } catch (e: Exception){
            println(e)
            NetworkResponse.Failure(e)
        }
    }

    override suspend fun changePassword(
        password: String,
        oldPassword: String
    ): ChangePassword {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$CHANGE_PASSWORD")
    }
}