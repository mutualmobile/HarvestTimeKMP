package com.mutualmobile.harvestKmp.android.di

import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { NewEntryScreenViewModel() }
}