package com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases

import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class GetUserUseCase(private val authApi: AuthApi) {
    suspend operator fun invoke(): NetworkResponse<GetUserResponse> {
        return authApi.getUser()
    }
}