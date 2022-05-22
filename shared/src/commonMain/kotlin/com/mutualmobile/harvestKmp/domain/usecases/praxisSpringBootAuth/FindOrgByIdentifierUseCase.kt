package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.FindOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FindOrgByIdentifierUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(identifier: String): NetworkResponse<FindOrgResponse> {
        return praxisSpringBootAPI.findOrgByIdentifier(identifier)
    }
}