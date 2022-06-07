package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.LoginFormValidator

class LoginUseCase(private val authApi: AuthApi) {
    suspend operator fun invoke(email: String, password: String): NetworkResponse<LoginResponse> {
        LoginFormValidator()(email = email, password = password)
        return authApi.login(email = email, password = password)
    }
}