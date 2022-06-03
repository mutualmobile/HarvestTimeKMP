package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindUsersInOrgUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        userType: kotlin.Int,
        orgIdentifier: kotlin.String?,
        isUserDeleted: kotlin.Boolean,
        offset: kotlin.Int,
        limit: kotlin.Int,
        searchName: kotlin.String?
    ): NetworkResponse<ApiResponse<Pair<Int, List<FindUsersInOrgResponse>>>> {
        return praxisSpringBootAPI.findUsersInOrg(
            userType,
            orgIdentifier,
            isUserDeleted,
            offset,
            limit,searchName
        )
    }
}