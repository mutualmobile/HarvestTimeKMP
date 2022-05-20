package com.mutualmobile.harvestKmp.datamodel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

abstract class PraxisDataModel(private val onDataState: (DataState) -> Unit) {
  protected val dataModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

  protected val dataState: MutableStateFlow<DataState> = MutableStateFlow(
    EmptyState
  )

  protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    dataState.value = ErrorState(throwable)
  }

  abstract fun activate()
  abstract fun destroy()
  abstract fun refresh()

  fun listenState() {
    dataModelScope.launch {
      dataState.collect {
        onDataState(it)
      }
    }
  }

}

