package com.mutualmobile.harvestKmp.android

import android.app.Application
import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val sharedComponent = SharedComponent()

class HKMPApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initSqlDelightExperimentalDependencies()
        GlobalScope.launch {
            precheckSqlite()
        }
    }

    private suspend fun precheckSqlite() {
        if (sharedComponent.provideHarvestUserLocal().driver == null) {
            val driver = DriverFactory(context = this).createDriverBlocking()
            sharedComponent.provideHarvestUserLocal().driver = driver
        }
    }
}