package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindUsersInOrgUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        userType: Int,
        orgIdentifier: String?,
        isUserDeleted: Boolean,
        offset: Int,
        limit: Int
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> {
        return praxisSpringBootAPI.findUsersInOrg(
            userType,
            orgIdentifier,
            isUserDeleted,
            offset,
            limit
        )
    }
}