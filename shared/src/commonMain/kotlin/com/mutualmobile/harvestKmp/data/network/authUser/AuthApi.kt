package com.mutualmobile.harvestKmp.data.network.authUser

import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface AuthApi {

    suspend fun fcmToken(user: User): NetworkResponse<ApiResponse<LoginResponse>>

    suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        company: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun newOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun logout(): NetworkResponse<ApiResponse<String>>

    suspend fun refreshToken(refreshToken: String): LoginResponse

    suspend fun getUser(): NetworkResponse<GetUserResponse>

    suspend fun changePassword(
        password: String,
        oldPassword: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun login(email: String, password: String): NetworkResponse<LoginResponse>

    suspend fun updateUser(id: String): User

}