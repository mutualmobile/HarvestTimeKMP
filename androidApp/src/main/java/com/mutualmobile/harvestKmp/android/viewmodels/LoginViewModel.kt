package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.LoginDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel : ViewModel() {
    var currentWorkEmail by mutableStateOf("")
    var currentPassword by mutableStateOf("")

    var currentNavigationCommand: PraxisCommand? by mutableStateOf(null)

    var currentLoginState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    val loginDataModel = LoginDataModel()

    var currentErrorMsg: String? by mutableStateOf(null)

    init {
        with(loginDataModel) {
            observeLoginDataFlow()
            observeLoginNavigationCommands()
        }
    }

    private fun LoginDataModel.observeLoginNavigationCommands() =
        praxisCommand.onEach { newCommand ->
            currentNavigationCommand = newCommand
        }.launchIn(viewModelScope)

    private fun LoginDataModel.observeLoginDataFlow() {
        dataFlow.onEach { loginState ->
            currentLoginState = loginState
        }.launchIn(viewModelScope)
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        currentWorkEmail = ""
        currentPassword = ""
        currentNavigationCommand = null
        currentLoginState = PraxisDataModel.EmptyState
        onComplete()
    }

}