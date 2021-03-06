package com.mutualmobile.harvestKmp.android.di

import com.mutualmobile.harvestKmp.android.viewmodels.ChangePasswordViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.ExistingOrgSignUpScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.FindWorkspaceViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.ForgotPasswordViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.LandingScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.LoginViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.MainActivityViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.NewOrgSignUpScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.ProjectScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.TimeScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.WorkTypeScreenViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { NewEntryScreenViewModel() }
    single { MainActivityViewModel() }
    single { LandingScreenViewModel() }
    single { TimeScreenViewModel() }
    single { FindWorkspaceViewModel() }
    single { LoginViewModel() }
    single { ChangePasswordViewModel() }
    single { ForgotPasswordViewModel() }
    single { ProjectScreenViewModel() }
    single { ExistingOrgSignUpScreenViewModel() }
    single { NewOrgSignUpScreenViewModel() }
    single { WorkTypeScreenViewModel() }
}