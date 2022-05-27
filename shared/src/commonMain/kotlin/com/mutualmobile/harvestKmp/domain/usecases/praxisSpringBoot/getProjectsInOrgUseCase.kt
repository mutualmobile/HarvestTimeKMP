package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindProjectsInOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class getProjectsInOrgUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ): NetworkResponse<ApiResponse<List<FindProjectsInOrgResponse>>> {
        return praxisSpringBootAPI.getProjectsInOrg(
            orgId,
            offset,
            limit
        )
    }
}