package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FindProjectsInOrgResponse(
    val client: String? = null,
    val endDate: String? = null,
    val isIndefinite: Boolean? = null,
    val name: String? = null,
    val organizationId: String? = null,
    val startDate: String? = null
)