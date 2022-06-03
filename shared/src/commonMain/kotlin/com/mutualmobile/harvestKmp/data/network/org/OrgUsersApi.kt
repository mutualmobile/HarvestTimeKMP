package com.mutualmobile.harvestKmp.data.network.org

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface OrgUsersApi {

    suspend fun findUsersInOrg(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>>

}