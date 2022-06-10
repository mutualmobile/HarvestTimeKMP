package com.mutualmobile.harvestKmp.domain.model.request

@kotlinx.serialization.Serializable
data class OrgProjectsRequest(
    val projectIds: List<String>
)