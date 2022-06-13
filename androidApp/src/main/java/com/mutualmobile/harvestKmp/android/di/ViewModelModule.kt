package com.mutualmobile.harvestKmp.android.di

import com.mutualmobile.harvestKmp.android.viewmodels.LandingScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.MainActivityViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.TimeScreenViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { NewEntryScreenViewModel() }
    single { MainActivityViewModel() }
    single { LandingScreenViewModel() }
    single { TimeScreenViewModel() }
}