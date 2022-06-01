package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend inline fun <reified T> getSafeNetworkResponse(
    onSuccessOperations: (NetworkResponse.Success<T>) -> Unit = {},
    networkOperation: () -> HttpResponse,
): NetworkResponse<T> {
    return try {
        val response = networkOperation()
        println("NetworkResponse: $response")
        when (response.status) {
            HttpStatusCode.OK -> {
                NetworkResponse.Success<T>(response.body()).apply(onSuccessOperations)
            }
            HttpStatusCode.Unauthorized -> {
                NetworkResponse.Unauthorized(Throwable("Failed to authorize"))
            }
            else -> {
                val responseMsg = response.body<ApiResponse<Unit>>().message
                NetworkResponse.Failure(Exception(responseMsg))
            }
        }
    } catch (e: Exception) {
        println(e)
        NetworkResponse.Failure(e)
    }
}