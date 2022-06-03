package com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindProjectsInOrgUseCase(private val orgProjectsApi: OrgProjectsApi) {
    suspend operator fun invoke(
        orgId: String?,
        offset: Int?,
        limit: Int?
    ): NetworkResponse<ApiResponse<Pair<Int, List<OrgProjectResponse>>>> {
        return orgProjectsApi.findProjectsInOrg(
            orgId = orgId,
            offset = offset,
            limit = limit
        )
    }
}