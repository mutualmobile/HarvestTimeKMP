package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.orgForgotPasswordApiDataModels.ForgotPasswordDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ForgotPasswordViewModel : ViewModel() {
    var forgotPasswordState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    var forgotPasswordNavigationCommand: PraxisCommand? by mutableStateOf(null)

    val forgotPasswordDataModel = ForgotPasswordDataModel()

    var currentWorkEmail by mutableStateOf("")

    init {
        with(forgotPasswordDataModel) {
            observeForgotPasswordState()
            observeForgotPasswordNavigationCommands()
        }
    }

    private fun ForgotPasswordDataModel.observeForgotPasswordNavigationCommands() =
        praxisCommand.onEach { newCommand ->
            forgotPasswordNavigationCommand = newCommand
        }.launchIn(viewModelScope)

    private fun ForgotPasswordDataModel.observeForgotPasswordState() {
        dataFlow.onEach { passwordState ->
            forgotPasswordState = passwordState
        }.launchIn(viewModelScope)
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        forgotPasswordState = PraxisDataModel.EmptyState
        forgotPasswordNavigationCommand = null
        currentWorkEmail = ""
        onComplete()
    }
}