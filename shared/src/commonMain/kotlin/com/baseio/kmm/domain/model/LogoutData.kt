package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LogoutData(
    val userId: String,
    val pushToken: String? = null
)