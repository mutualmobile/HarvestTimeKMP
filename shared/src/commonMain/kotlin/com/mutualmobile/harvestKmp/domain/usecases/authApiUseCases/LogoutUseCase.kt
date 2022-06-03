package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.local.HarvestUserLocal
import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin

class LogoutUseCase(
    private val authApi: AuthApi,
    private val settings: Settings,
    private val httpClient: HttpClient,
    private val harvestUserLocal: HarvestUserLocal
) {
    suspend operator fun invoke(): NetworkResponse<ApiResponse<String>> {
        return authApi.logout().apply {
            settings.clear()
            harvestUserLocal.clear()
            httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().first()
                .clearToken() // is this necessary ? When does Ktor clear the bearer token ?
        }
    }
}