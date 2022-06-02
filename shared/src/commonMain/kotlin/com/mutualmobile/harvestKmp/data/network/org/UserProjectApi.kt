package com.mutualmobile.harvestKmp.data.network.org

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface UserProjectApi {

    suspend fun assignProjectsToUsers(projectMap:HashMap<String,List<String>>): NetworkResponse<ApiResponse<Unit>>

}