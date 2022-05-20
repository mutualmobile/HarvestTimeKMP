package com.mutualmobile.harvestKmp.domain.model.response

import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import kotlinx.serialization.Serializable

@Serializable
data class FindOrgResponse(
    val `data`: HarvestOrganization?,
    val message: String
)

//@Serializable
//data class Data(
//    val id: String,
//    val identifier: String,
//    val imgUrl: String,
//    val name: String,
//    val website: String
//)
