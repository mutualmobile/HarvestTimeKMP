package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class HarvestUserWorkRequest(
    val id: String? = null,
    val projectId: String,
    val userId: String,
    val workDate: String,
    val workHours: Float,
    val note: String? = null
)