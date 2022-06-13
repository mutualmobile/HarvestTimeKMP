package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ExistingOrgSignUpScreenViewModel : ViewModel() {
    var currentWorkEmail by mutableStateOf("")
    var currentPassword by mutableStateOf("")
    var currentConfirmPassword by mutableStateOf("")
    var currentFirstName by mutableStateOf("")
    var currentLastName by mutableStateOf("")

    var currentSignUpState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    var signUpPraxisCommand: PraxisCommand? by mutableStateOf(null)
    val signUpDataModel = SignUpDataModel().apply {
        observeDataState()
        observeNavigationCommands()
    }

    private fun SignUpDataModel.observeNavigationCommands() {
        praxisCommand.onEach { newCommand ->
            signUpPraxisCommand = newCommand
        }.launchIn(viewModelScope)
    }

    private fun SignUpDataModel.observeDataState() {
        dataFlow.onEach { signUpState ->
            currentSignUpState = signUpState
        }.launchIn(viewModelScope)
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        currentWorkEmail = ""
        currentPassword = ""
        currentConfirmPassword = ""
        currentFirstName = ""
        currentLastName = ""
        currentSignUpState = PraxisDataModel.EmptyState
        signUpPraxisCommand = null
        onComplete()
    }
}