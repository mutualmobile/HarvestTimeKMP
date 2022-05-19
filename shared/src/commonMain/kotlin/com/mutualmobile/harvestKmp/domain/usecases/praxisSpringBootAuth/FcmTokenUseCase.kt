package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class FcmTokenUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(): NetworkResponse<LoginResponse> {
        return praxisSpringBootAPI.fcmToken()
    }
}