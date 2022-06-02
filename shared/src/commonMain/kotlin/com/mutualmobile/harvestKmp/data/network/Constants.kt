package com.mutualmobile.harvestKmp.data.network

object Constants {
    const val JWT_TOKEN = "JWT_TOKEN"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
}

enum class UserRole(val role: String) {
    ORG_ADMIN("1"),
    ORG_USER("2"),
    HARVEST_SUPER_ADMIN("3")
}