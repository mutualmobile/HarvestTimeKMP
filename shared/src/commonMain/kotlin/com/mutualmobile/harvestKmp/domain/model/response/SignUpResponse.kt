package com.mutualmobile.harvestKmp.domain.model.response

data class SignUpResponse(
    val token : String,
    val message : String,
    val refreshToken : String
)
