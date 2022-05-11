package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

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