package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String
)