package com.mutualmobile.harvestKmp.datamodel

import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse

sealed class DataState
object LoadingState : DataState()
object EmptyState : DataState()
object Complete : DataState()
