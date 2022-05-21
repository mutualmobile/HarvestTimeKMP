package com.mutualmobile.harvestKmp.datamodel

open class DataState
object LoadingState : DataState()
object EmptyState : DataState()
object Complete : DataState()
data class SuccessState<T>(
    val data: T,
) : DataState()
class ErrorState(var throwable: Throwable) : DataState()