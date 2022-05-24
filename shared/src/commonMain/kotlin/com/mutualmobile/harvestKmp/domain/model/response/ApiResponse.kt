package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null,
    @SerialName("token")
    val jwt_token : String? = null,
    val refreshToken : String? = null
)

