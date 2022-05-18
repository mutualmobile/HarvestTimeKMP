package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String = "",
    val password: String = "",
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)