package com.mutualmobile.harvestKmp.domain.usecases

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get

class CurrentUserLoggedInUseCase(private val settings: Settings) {
    operator fun invoke(): Boolean {
        return settings.get("JWT_TOKEN", defaultValue = "").isNotEmpty()
    }
}