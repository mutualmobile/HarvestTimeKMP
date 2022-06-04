package com.mutualmobile.harvestKmp.data.network.org

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface UserProjectApi {

    suspend fun assignProjectsToUsers(
        projectMap: HashMap<String, List<String>>
    ): NetworkResponse<ApiResponse<Unit>>

    suspend fun logWorkTime(
        id: String? = null,
        projectId: String,
        userId: String,
        workDate: String,
        workHours: Float,
        note: String? = null
    ): NetworkResponse<ApiResponse<Unit>>

    suspend fun getUserAssignedProjects(
        userId: String? = null
    ): NetworkResponse<ApiResponse<List<OrgProjectResponse>>>

}
