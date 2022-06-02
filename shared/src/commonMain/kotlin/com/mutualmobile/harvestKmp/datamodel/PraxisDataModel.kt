package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.di.SharedComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.getScopeName

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
}

