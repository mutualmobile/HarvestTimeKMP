package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePassword(
    val password: String,
    val oldPassword: String
)