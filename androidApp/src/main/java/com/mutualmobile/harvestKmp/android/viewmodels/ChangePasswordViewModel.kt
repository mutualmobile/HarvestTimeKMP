package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.ChangePasswordDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChangePasswordViewModel : ViewModel() {
    var changePasswordState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    var changePasswordPraxisCommand: PraxisCommand? by mutableStateOf(null)

    val changePasswordDataModel = ChangePasswordDataModel()

    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")

    init {
        with(changePasswordDataModel) {
            observeChangePasswordNavigationCommands()
        }
    }

    private fun ChangePasswordDataModel.observeChangePasswordNavigationCommands() =
        praxisCommand.onEach { newCommand ->
            changePasswordPraxisCommand = newCommand
        }.launchIn(viewModelScope)

    fun resetAll(onComplete: () -> Unit = {}) {
        changePasswordState = PraxisDataModel.EmptyState
        changePasswordPraxisCommand = null
        onComplete()
    }
}