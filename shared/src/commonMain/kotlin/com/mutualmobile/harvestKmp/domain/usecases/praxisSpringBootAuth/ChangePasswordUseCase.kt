package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.ChangePassword
import com.mutualmobile.harvestKmp.domain.model.response.ChangePasswordResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ChangePasswordUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(password: String, oldPassword: String, token: String): NetworkResponse<ChangePasswordResponse>  {
        return praxisSpringBootAPI.changePassword(password, oldPassword, token)
    }
}