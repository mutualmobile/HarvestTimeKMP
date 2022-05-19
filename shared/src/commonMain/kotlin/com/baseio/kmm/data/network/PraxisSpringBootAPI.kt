package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.request.ChangePassword
import com.baseio.kmm.domain.model.request.LogoutData
import com.baseio.kmm.domain.model.request.RefreshToken
import com.baseio.kmm.domain.model.request.User
import com.baseio.kmm.domain.model.response.SuccessResponse
import com.baseio.kmm.features.NetworkResponse

interface PraxisSpringBootAPI {

    suspend fun getUser(id: String): User

    suspend fun putUser(id: String): User

    suspend fun refreshToken(refreshToken: String): RefreshToken

    suspend fun signup(email: String, password: String): NetworkResponse<SuccessResponse>

    suspend fun login(email: String, password: String): NetworkResponse<SuccessResponse>

    suspend fun logout(userId: String): LogoutData

    suspend fun fcmToken(): NetworkResponse<SuccessResponse>

    suspend fun changePassword(password: String, oldPassword: String): ChangePassword

}
