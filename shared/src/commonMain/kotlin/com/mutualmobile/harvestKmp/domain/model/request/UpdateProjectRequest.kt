package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val id: String?= null,
    val name: String?= null,
    val client: String?= null,
    val startDate: String?= null,
    val endDate: String?= null,
    val isIndefinite: Boolean?,
    val organizationId: String?= null
)