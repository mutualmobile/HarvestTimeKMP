package com.baseio.kmm.domain.model

data class SuccessResponse(
    val token : String,
    val message : String,
    val refreshToken : String
)
