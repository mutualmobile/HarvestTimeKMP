package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val modifiedTime: String,
    val orgId: String,
    val role: String
)