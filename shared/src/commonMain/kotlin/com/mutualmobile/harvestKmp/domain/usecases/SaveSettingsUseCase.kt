package com.mutualmobile.harvestKmp.domain.usecases

import com.mutualmobile.harvestKmp.data.network.Constants
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SaveSettingsUseCase(private val settings: Settings) {
    operator fun invoke(token: String?, refreshToken: String?) {
        settings[Constants.JWT_TOKEN] = token ?: ""
        settings[Constants.REFRESH_TOKEN] = refreshToken ?: ""
    }
}