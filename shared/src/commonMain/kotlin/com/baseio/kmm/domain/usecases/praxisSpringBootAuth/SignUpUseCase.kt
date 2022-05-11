package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.SignUpData

class SignUpUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(email: String, password: String): SignUpData {
        return praxisSpringBootAPI.signup(email, password)
    }
}