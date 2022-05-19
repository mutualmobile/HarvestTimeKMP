package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.baseio.kmm.features.NetworkResponse

class SignUpUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(email: String, password: String): NetworkResponse<LoginResponse> {
        return praxisSpringBootAPI.signup(email, password)
    }
}