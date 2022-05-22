package com.mutualmobile.harvestKmp.domain.usecases

interface BaseUseCase<in IN, out OUT> {
    suspend fun perform(input: IN?): OUT?
}