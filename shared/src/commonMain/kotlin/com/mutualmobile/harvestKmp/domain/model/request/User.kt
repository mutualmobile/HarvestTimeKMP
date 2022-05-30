package com.mutualmobile.harvestKmp.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: DevicePlatform? = null,
    val orgId: String? = null,
    val harvestOrganization: HarvestOrganization? = null
)

@Serializable
enum class DevicePlatform {
    Android,
    iOS,
    Web
}