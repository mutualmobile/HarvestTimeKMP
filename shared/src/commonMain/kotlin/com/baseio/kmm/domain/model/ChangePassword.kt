package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangePassword(
    val password: String,
    val oldPassword: String
)