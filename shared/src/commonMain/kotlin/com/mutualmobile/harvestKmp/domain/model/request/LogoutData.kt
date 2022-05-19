package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutData(
    val userId: String? = null,
    val pushToken: String? = null
)