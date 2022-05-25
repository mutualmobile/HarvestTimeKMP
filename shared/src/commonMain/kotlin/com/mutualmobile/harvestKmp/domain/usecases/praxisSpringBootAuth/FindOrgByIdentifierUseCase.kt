package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindOrgByIdentifierUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(identifier: String): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return praxisSpringBootAPI.findOrgByIdentifier(identifier)
    }
}