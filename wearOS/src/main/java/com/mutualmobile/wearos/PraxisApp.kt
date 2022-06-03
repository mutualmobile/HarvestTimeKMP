package com.mutualmobile.wearos

import android.app.Application
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies

val sharedComponent = SharedComponent()
val useCasesComponent = SpringBootAuthUseCasesComponent()

class PraxisApp : Application() {
  override fun onCreate() {
    super.onCreate()
    initSqlDelightExperimentalDependencies()
  }
}