package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    val email: String? = null,
    val firstName: String? = null,
    val id: String? = null,
    val lastName: String? = null,
    val modifiedTime: String? = null,
    val orgId: String? = null,
    val role: String? = null
)