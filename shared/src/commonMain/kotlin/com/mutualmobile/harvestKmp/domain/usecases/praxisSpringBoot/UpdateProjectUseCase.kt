package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.ProjectCrudValidator
import kotlinx.datetime.LocalDate

class UpdateProjectUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        id: String,
        name: String,
        client: String,
        startDate: String,
        endDate: String?,
        isIndefinite: Boolean,
        organizationId: String
    ): NetworkResponse<ApiResponse<Unit>> {
        ProjectCrudValidator()(name, client)
        return praxisSpringBootAPI.updateProject(
            id,
            name,
            client,
            startDate,
            endDate,
            isIndefinite,
            organizationId
        )
    }
}