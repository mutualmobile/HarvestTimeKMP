package com.mutualmobile.harvestKmp.di

import com.mutualmobile.harvestKmp.db.DriverFactory
import org.koin.dsl.module
import io.ktor.client.engine.android.*

actual fun platformModule() = module {
    single { Android.create() }
    single {
        DriverFactory(get()).createDriver()
    }
}

