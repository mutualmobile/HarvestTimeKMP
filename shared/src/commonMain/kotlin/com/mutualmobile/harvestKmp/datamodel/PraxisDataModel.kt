package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.di.SharedComponent
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import kotlinx.coroutines.*

abstract class PraxisDataModel(
    @NativeCoroutineScope
    var dataModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    protected val settings = SharedComponent().provideSettings()

    var praxisCommand: (PraxisCommand) -> Unit = {}

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
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

