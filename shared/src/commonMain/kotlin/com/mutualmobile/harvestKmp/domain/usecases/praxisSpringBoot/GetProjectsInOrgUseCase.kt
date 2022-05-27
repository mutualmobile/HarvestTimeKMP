package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetProjectsInOrgUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ): NetworkResponse<ApiResponse<List<OrgProjectResponse>>> {
        return praxisSpringBootAPI.getProjectsInOrg(
            orgId = orgId,
            offset = offset,
            limit = limit
        )
    }
}