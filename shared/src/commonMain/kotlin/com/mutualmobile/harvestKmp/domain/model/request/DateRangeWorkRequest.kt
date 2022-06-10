package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DateRangeWorkRequest(
    val startDate: String,
    val endDate: String,
    val userIds: List<String>? = null
)
