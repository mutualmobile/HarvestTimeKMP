package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateProject(
    val client: String,
    val endDate: String,
    val isIndefinite: Boolean,
    val name: String,
    val startDate: String
)