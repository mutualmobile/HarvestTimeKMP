package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.LogoutData

class LogoutUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(userId: String): LogoutData {
        return praxisSpringBootAPI.logout(userId)
    }
}