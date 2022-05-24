package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.request.LogoutData
import com.mutualmobile.harvestKmp.domain.model.request.RefreshToken
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface PraxisSpringBootAPI {

    suspend fun getUser(id: String): User

    suspend fun putUser(id: String): User

    suspend fun refreshToken(refreshToken: String): RefreshToken

    suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun newOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun login(email: String, password: String): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun logout(userId: String): LogoutData

    suspend fun fcmToken(): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun changePassword(password: String, oldPassword: String): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>>

}
