package com.mutualmobile.harvestKmp.domain.model.request

data class DateRangeWorkRequest(
    val startDate: String,
    val endDate: String,
    val userIds: List<String>? = null
)
