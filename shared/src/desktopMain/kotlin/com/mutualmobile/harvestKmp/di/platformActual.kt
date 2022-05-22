package com.mutualmobile.harvestKmp.di

import com.mutualmobile.harvestKmp.db.DriverFactory
import io.ktor.client.engine.java.*
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Java.create() }
    single {
        DriverFactory().createDriver()
    }
}

