package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.LoginData

class LoginUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(email: String, password: String): LoginData {
        return praxisSpringBootAPI.login(email, password)
    }
}