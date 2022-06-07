package com.mutualmobile.harvestKmp.data.network.org

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface OrgProjectsApi {

    suspend fun createProject(
        name: String,
        client: String,
        isIndefinite: Boolean,
        startDate: String,
        endDate: String?
    ): NetworkResponse<ApiResponse<OrgProjectResponse>>

    suspend fun findProjectsInOrg(
        orgId: String?,
        offset: Int?,
        limit: Int?,
        search: String?
    ): NetworkResponse<ApiResponse<Pair<Int, List<OrgProjectResponse>>>>

    suspend fun updateProject(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String?,
        isIndefinite: Boolean,
        organizationId: String
    ): NetworkResponse<ApiResponse<Unit>>

    suspend fun deleteProject(
        projectId: String
    ): NetworkResponse<ApiResponse<Unit>>

    suspend fun getListOfUsersForAProject(
        projectId: String
    ): NetworkResponse<ApiResponse<List<GetUserResponse>>>

}
