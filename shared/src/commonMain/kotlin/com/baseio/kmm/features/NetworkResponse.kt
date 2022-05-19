package com.baseio.kmm.features

sealed class NetworkResponse<out T> {
    class Success<T>(val data : T) : NetworkResponse<T>()
    class Failure(val exception : Exception) : NetworkResponse<Nothing>()
}