package com.mutualmobile.harvestKmp.domain.model.response

data class LoginResponse(
    val token : String,
    val message : String,
    val refreshToken : String
)
