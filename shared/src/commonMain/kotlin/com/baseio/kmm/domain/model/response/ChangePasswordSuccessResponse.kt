package com.baseio.kmm.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordSuccessResponse(
    val message: String? =null,
//    val data : List<Data>? = null
)

//@Serializable
//data class Data(
//    val data: String? = ""
//)
