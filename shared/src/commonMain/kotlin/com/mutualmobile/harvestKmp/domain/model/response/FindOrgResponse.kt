package com.mutualmobile.harvestKmp.domain.model.response

import kotlinx.serialization.Serializable

//TODO rename this file
@Serializable
data class ApiResponse<T>(val message: String? = null, val data: T? = null)

//@Serializable
//data class Data(
//    val id: String,
//    val identifier: String,
//    val imgUrl: String,
//    val name: String,
//    val website: String
//)
