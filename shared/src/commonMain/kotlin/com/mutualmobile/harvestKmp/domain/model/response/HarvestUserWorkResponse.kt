package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HarvestUserWorkResponse(
    val id: String? = null,
    val projectId: String,
    val userId: String,
    val workDate: String,
    val workHours: Float,
    val note: String? = null
)