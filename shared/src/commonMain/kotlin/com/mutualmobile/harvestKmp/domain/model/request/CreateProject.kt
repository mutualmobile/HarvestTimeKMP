package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateProject(
    val client: String? = null,
    val endDate: String? = null,
    val isIndefinite: Boolean? = null,
    val name: String? = null,
    val startDate: String? = null
)