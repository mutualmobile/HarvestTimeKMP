package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.di.SharedComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

abstract class PraxisDataModel(
    onDataState: (DataState) -> Unit,
    var dataModelScope: CoroutineScope = MainScope()
) {

    protected val settings = SharedComponent().provideSettings()

    var praxisCommand: (PraxisCommand) -> Unit = {}

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onDataState(ErrorState(throwable))
        println(throwable)
        throwable.printStackTrace()
        praxisCommand(
            ModalPraxisCommand(
                title = "Error",
                throwable.message ?: "An Unknown error has happened"
            )
        )
    }

    abstract fun activate()
    abstract fun destroy()
    abstract fun refresh()

    sealed class DataState
    object LoadingState : DataState()
    object EmptyState : DataState()
    object Complete : DataState()
    data class SuccessState<T>(
        val data: T,
    ) : DataState()

    class ErrorState(var throwable: Throwable) : DataState()
    object LogoutInProgress : DataState()
}

