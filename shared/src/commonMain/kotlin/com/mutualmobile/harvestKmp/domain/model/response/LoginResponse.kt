package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token : String,
    val message : String,
    val refreshToken : String
)
