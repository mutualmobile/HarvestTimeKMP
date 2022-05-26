package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectResponse(
    val client: String,
    val endDate: String,
    val isIndefinite: Boolean,
    val name: String,
    val startDate: String
)