package com.mutualmobile.harvestKmp.datamodel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.getScopeName

abstract class PraxisDataModel(onDataState: (DataState) -> Unit) {
    protected val dataModelScope = MainScope()
    var praxisCommand: (PraxisCommand) -> Unit = {}
    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onDataState(ErrorState(throwable))
    }

    abstract fun activate()
    abstract fun destroy()
    abstract fun refresh()
}

