package com.mutualmobile.harvestKmp.features

sealed class NetworkResponse<out T> {
    class Success<T>(val data : T) : NetworkResponse<T>()
    class Failure(val exception : Throwable) : NetworkResponse<Nothing>()
}