package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.response.SuccessResponse
import com.baseio.kmm.features.NetworkResponse

class FcmTokenUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(): NetworkResponse<SuccessResponse> {
        return praxisSpringBootAPI.fcmToken()
    }
}