package com.mutualmobile.harvestKmp.domain.usecases.orgUsersApiUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgUsersApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindUsersInOrgUseCase(private val orgUsersApi: OrgUsersApi) {
    suspend operator fun invoke(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int,
        searchName: kotlin.String?
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> {
        return orgUsersApi.findUsersInOrg(
            userType = userType,
            orgIdentifier = orgIdentifier,
            isUserDeleted = isUserDeleted,
            offset = offset,
            limit = limit,
            searchName
        )
    }
}