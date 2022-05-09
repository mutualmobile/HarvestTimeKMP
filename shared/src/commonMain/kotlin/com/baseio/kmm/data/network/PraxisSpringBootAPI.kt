package com.baseio.kmm.data.network

import com.baseio.kmm.domain.model.*

interface PraxisSpringBootAPI {
    suspend fun login(): List<LoginData>

    suspend fun signup(): List<SignUpData>

    suspend fun logout(): List<LogoutData>

    suspend fun changePassword(): List<ChangePassword>

    suspend fun getUser(): List<User>
}
