package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.request.ChangePassword

class ChangePasswordUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(password: String, oldPassword: String): ChangePassword {
        return praxisSpringBootAPI.changePassword(password, oldPassword)
    }
}