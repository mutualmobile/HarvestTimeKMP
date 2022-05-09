package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*
import io.ktor.client.*
import io.ktor.client.request.*

const val SPRING_BOOT_BASE_URL = "http://localhost:5001"
const val API_URL = "/api/v1"
const val LOGIN = "/public/login"
const val SIGNUP = "/public/signup"
const val LOGOUT = "/logout"
const val CHANGE_PASSWORD = "/changePassword"
const val USER = "/user"

class PraxisSpringBootAPIImpl(private val httpClient: HttpClient): PraxisSpringBootAPI {

    override suspend fun login(): List<LoginData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGIN")
    }

    override suspend fun signup(): List<SignUpData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$SIGNUP")
    }

    override suspend fun logout(): List<LogoutData> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$LOGOUT")
    }

    override suspend fun changePassword(): List<ChangePassword> {
        return httpClient.post("$SPRING_BOOT_BASE_URL$API_URL$CHANGE_PASSWORD")
    }

    override suspend fun getUser(): List<User> {
        return httpClient.get("$SPRING_BOOT_BASE_URL$API_URL$USER")

    }
}