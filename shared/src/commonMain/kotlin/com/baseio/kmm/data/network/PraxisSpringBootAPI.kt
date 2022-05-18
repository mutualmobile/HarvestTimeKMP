package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*
import com.baseio.kmm.features.NetworkResponse

interface PraxisSpringBootAPI {

    suspend fun getUser(id: String): User

    suspend fun putUser(id: String): User

    suspend fun refreshToken(refreshToken: String): RefreshToken

    suspend fun signup(email: String, password: String): NetworkResponse<SuccessResponse>

    suspend fun login(email: String, password: String): NetworkResponse<SuccessResponse>

    suspend fun logout(userId: String): LogoutData

    suspend fun fcmToken(): FcmToken

    suspend fun changePassword(password: String, oldPassword: String): ChangePassword

}
