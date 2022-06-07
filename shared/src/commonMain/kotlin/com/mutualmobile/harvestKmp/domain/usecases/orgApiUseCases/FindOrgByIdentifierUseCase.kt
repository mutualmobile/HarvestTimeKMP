package com.mutualmobile.harvestKmp.domain.usecases.orgApiUseCases

import com.mutualmobile.harvestKmp.data.network.org.OrgApi
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindOrgByIdentifierUseCase(private val orgApi: OrgApi) {
    suspend operator fun invoke(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return orgApi.findOrgByIdentifier(identifier = identifier)
    }
}