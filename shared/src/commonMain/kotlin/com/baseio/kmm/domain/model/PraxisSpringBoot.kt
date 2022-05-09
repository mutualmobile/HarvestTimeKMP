package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

data class PraxisSpringBoot(
    val name: String
)

@Serializable
data class User(
    val id: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)

@Serializable
data class RefreshToken(
    val refreshToken: String
)

@Serializable
data class SignUpData(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)

@Serializable
data class LoginData(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val password: String,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)

@Serializable
data class LogoutData(
    val userId: String,
    val pushToken: String? = null
)

@Serializable
data class FcmToken(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)

@Serializable
data class ChangePassword(
    val password: String,
    val oldPassword: String
)
