package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*

interface PraxisSpringBootAPI {
    suspend fun getUser(): List<User>

    suspend fun putUser(): List<User>

    suspend fun refreshToken(): List<RefreshToken>

    suspend fun signup(): List<SignUpData>

    suspend fun login(): List<LoginData>

    suspend fun logout(): List<LogoutData>

    suspend fun fcmToken(): List<FcmToken>

    suspend fun changePassword(): List<ChangePassword>
}
