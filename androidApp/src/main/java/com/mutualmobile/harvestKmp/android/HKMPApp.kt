package com.mutualmobile.harvestKmp.android

import android.app.Application
import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSharedDependencies
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext

val sharedComponent = SharedComponent()

class HKMPApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initSharedDependencies().apply {
            androidContext(this@HKMPApp)
        }
        GlobalScope.launch {
            precheckSqlite()
        }
    }

    private suspend fun precheckSqlite() {
        if (sharedComponent.provideGithubTrendingLocal().driver == null) {
            val driver = DriverFactory(context = this).createDriverBlocking()
            sharedComponent.provideGithubTrendingLocal().driver = driver
        }
    }
}