package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings

class LogoutUseCase(
    private val praxisSpringBootAPI: PraxisSpringBootAPI,
    private val settings: Settings
) {
    suspend operator fun invoke(): NetworkResponse<ApiResponse<String>> {
        return praxisSpringBootAPI.logout().apply {
            settings.clear()
        }
    }
}