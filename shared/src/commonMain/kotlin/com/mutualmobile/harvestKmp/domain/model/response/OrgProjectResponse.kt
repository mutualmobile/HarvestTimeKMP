package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OrgProjectResponse(
    var client: String? = null,
    var endDate: String? = null,
    val isIndefinite: Boolean? = null,
    var name: String? = null,
    var startDate: String? = null,
    val id: String? = null,
    val organizationId: String? = null
)