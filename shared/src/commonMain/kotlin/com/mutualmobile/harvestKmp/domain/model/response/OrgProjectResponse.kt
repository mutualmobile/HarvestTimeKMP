package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OrgProjectResponse(
    val client: String? = null,
    val endDate: String? = null,
    val isIndefinite: Boolean? = null,
    val name: String? = null,
    val startDate: String? = null,
    val id: String? = null,
    val organizationId: String? = null
)