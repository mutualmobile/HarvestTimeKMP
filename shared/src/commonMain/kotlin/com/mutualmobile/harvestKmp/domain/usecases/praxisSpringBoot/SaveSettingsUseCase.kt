package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.di.networkModule
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class SaveSettingsUseCase(private val settings: Settings) {
    operator fun invoke(token: String?, refreshToken: String?) {
        settings[Constants.JWT_TOKEN] = token ?: ""
        settings[Constants.REFRESH_TOKEN] = refreshToken ?: ""
        unloadKoinModules(networkModule)
        loadKoinModules(networkModule)
    }

}