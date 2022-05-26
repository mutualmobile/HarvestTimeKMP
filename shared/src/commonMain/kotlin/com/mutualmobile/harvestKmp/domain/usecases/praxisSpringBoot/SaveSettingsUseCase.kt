package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SaveSettingsUseCase(private val settings: Settings) {
    operator fun invoke(token: String, refreshToken: String) {
        settings["JWT_TOKEN"] = token
        settings["REFRESH_TOKEN"] = refreshToken
    }

}