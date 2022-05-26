package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class HarvestOrganization(
    val id: String? = null,
    val imgUrl: String? = null,
    val name: String? = null,
    val website: String? = null,
    val identifier: String? = null
)