package com.baseio.kmm.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String
)