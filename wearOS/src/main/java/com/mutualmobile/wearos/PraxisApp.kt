package com.mutualmobile.wearos

import android.app.Application
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies

val sharedComponent = SharedComponent()
val useCasesComponent = UseCasesComponent()

class PraxisApp : Application() {
  override fun onCreate() {
    super.onCreate()
    initSqlDelightExperimentalDependencies()
  }
}