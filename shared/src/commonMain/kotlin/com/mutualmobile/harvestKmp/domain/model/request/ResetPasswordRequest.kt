package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(var token:String?, var password:String?)