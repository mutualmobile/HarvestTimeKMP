package com.mutualmobile.harvestKmp.features.harvest

sealed class DataState
object LoadingState : DataState()
object EmptyState : DataState()
object Complete : DataState()
