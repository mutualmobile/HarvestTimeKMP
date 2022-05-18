package com.baseio.kmm.features

import io.ktor.client.features.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GenericResponse {
}

//const val GenericNetworkResponse = "Unexpected Error Occurred!"
//const val NoInternetMsg = "Please check your internet connection!"
//
//suspend fun <T> safeAPICall(networkStatus: NetworkStatus, block: suspend () -> T): NetworkResponse<out T> {
//    return when (networkStatus) {
//        NetworkStatus.Available -> {
//            try {
//                NetworkResponse.Success(data = block())
//            } catch (e: Exception) {
//                NetworkResponse.Failure(
//                    reason = when (e) {
//                        is ResponseException -> e.response.content.readUTF8Line()?.let {
//                            Json.decodeFromString<DGenericResponse>(it).message
//                        }
//                        else -> e.localizedMessage
//                    } ?: GenericNetworkResponse
//                )
//            }
//        }
//        NetworkStatus.Unavailable -> {
//            NetworkResponse.Failure(reason = NoInternetMsg)
//        }
//        else -> {}
//    }
//}