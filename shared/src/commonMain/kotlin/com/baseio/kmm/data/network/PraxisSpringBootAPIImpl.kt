package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*
import io.ktor.client.*
import io.ktor.client.request.*

const val SPRING_BOOT_BASE_URL = "http://localhost:5001"
const val API_URL = "/api/v1"

const val GET_USER = "/user"
const val PUT_USER = "/user"
const val REFRESH_TOKEN = "/refreshToken"
const val SIGNUP = "/public/signup"
const val LOGIN = "/public/login"
const val LOGOUT = "/logout"
const val FCM_TOKEN = "/fcmToken"
const val CHANGE_PASSWORD = "/changePassword"

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient): PraxisSpringBootAPI {

    override suspend fun getUser(): List<User> {
        return httpClient.get("$SPRING_BOOT_BASE_URL$API_URL$GET_USER")
    }

    override suspend fun putUser(): List<User> {
        return httpClient.put("$SPRING_BOOT_BASE_URL$API_URL$PUT_USER")
    }

    override suspend fun refreshToken(): List<RefreshToken> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$REFRESH_TOKEN")
    }

    override suspend fun signup(): List<SignUpData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$SIGNUP")
    }

    override suspend fun login(): List<LoginData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGIN")
    }

    override suspend fun logout(): List<LogoutData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGOUT")
    }

    override suspend fun fcmToken(): List<FcmToken> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$FCM_TOKEN")
    }

    override suspend fun changePassword(): List<ChangePassword> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$CHANGE_PASSWORD")
    }
}