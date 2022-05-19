package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.SignUpResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class SignUpUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(email: String, password: String): NetworkResponse<SignUpResponse> {
        return praxisSpringBootAPI.signup(email, password)
    }
}