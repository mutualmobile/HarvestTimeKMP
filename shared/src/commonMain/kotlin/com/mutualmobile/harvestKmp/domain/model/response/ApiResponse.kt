package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null,
)

@Serializable
data class SpringError(
    val error: String? = null,
    val path: String? = null,
    val status: Int? = null,
    val timestamp: String? = null
)