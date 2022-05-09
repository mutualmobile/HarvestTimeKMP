package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*

interface PraxisSpringBootAPI {

    suspend fun getUser(id: String): User

    suspend fun putUser(id: String): User

    suspend fun refreshToken(refreshToken: String): RefreshToken

    suspend fun signup(firstName: String, lastName: String, email: String, password: String): SignUpData

    suspend fun login(email: String, password: String): LoginData

    suspend fun logout(userId: String): LogoutData

    suspend fun fcmToken(): FcmToken

    suspend fun changePassword(password: String, oldPassword: String): ChangePassword

}
