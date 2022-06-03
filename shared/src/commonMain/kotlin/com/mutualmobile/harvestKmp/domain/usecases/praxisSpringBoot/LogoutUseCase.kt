package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.local.HarvestUserLocal
import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

class LogoutUseCase(
    private val praxisSpringBootAPI: PraxisSpringBootAPI,
    private val settings: Settings,
    private val httpClient: HttpClient,
    private val harvestUserLocal: HarvestUserLocal
) {
    suspend operator fun invoke(): NetworkResponse<ApiResponse<String>> {
        return praxisSpringBootAPI.logout().apply {
            settings.clear()
            harvestUserLocal.clear()
            httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().first()
                .clearToken() // is this necessary ? When does Ktor clear the bearer token ?
        }
    }
}