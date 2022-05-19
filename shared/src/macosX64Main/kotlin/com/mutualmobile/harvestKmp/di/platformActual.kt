package com.mutualmobile.harvestKmp.di

import com.mutualmobile.harvestKmp.db.DriverFactory
import org.koin.dsl.module
import io.ktor.client.engine.ios.*

actual fun platformModule() = module {
    single { Ios.create() }
    single {
        DriverFactory().createDriver()
    }
}

