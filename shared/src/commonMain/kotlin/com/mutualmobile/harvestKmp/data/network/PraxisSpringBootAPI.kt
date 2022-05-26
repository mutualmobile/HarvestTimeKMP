package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.request.*
import com.mutualmobile.harvestKmp.domain.model.response.*
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface PraxisSpringBootAPI {

    suspend fun getUser(): NetworkResponse<ApiResponse<GetUserResponse>>

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
        orgIdentifier: String,
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun login(email: String, password: String): NetworkResponse<LoginResponse>

    suspend fun logout(): NetworkResponse<LogoutData>

    suspend fun fcmToken(): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun changePassword(
        password: String,
        oldPassword: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun forgotPassword(email: String): NetworkResponse<ApiResponse<HarvestOrganization>>

    suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResponse<ApiResponse<Unit>>

    suspend fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String
    ): NetworkResponse<ApiResponse<CreateProjectResponse>>

    suspend fun findUsersInOrg(
        userType: Int,
        orgIdentifier: String,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ): NetworkResponse<ApiResponse<List<FindUsersInOrgResponse>>>

}
