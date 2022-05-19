package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.LogoutData

class LogoutUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(userId: String): LogoutData {
        return praxisSpringBootAPI.logout(userId)
    }
}