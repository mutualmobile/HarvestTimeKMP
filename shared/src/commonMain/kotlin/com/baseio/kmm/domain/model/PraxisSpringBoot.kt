package com.baseio.kmm.domain.model

data class PraxisSpringBoot(
    val name: String
)

data class LoginData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val pushToken: String,
    val profilePic: String,
    val modifiedTime: String,
    val platform: String
)

data class LogoutData(
    val userId: String,
    val pushToken: String
)

data class SignUpData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val pushToken: String,
    val profilePic: String,
    val modifiedTime: String,
    val platform: String
)

data class ChangePassword(
    val password: String,
    val oldPassword: String
)

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val pushToken: String,
    val profilePic: String,
    val modifiedTime: String,
    val platform: String
)
