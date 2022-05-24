package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordResponse(
    var message: String? = null
)