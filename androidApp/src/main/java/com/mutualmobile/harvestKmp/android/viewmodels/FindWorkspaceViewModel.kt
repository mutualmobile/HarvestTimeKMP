package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.orgApiDataModels.FindOrgByIdentifierDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FindWorkspaceViewModel : ViewModel() {
    val findOrgByIdentifierDataModel = FindOrgByIdentifierDataModel()

    var tfValue by mutableStateOf("")

    var currentFindOrgNavigationCommand: PraxisCommand? by mutableStateOf(null)

    var findOrgState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    init {
        with(findOrgByIdentifierDataModel) {
            observeFindOrgDataFlow()
            observeFindOrgNavigationCommands()
            activate()
        }
    }

    private fun FindOrgByIdentifierDataModel.observeFindOrgNavigationCommands() {
        praxisCommand.onEach { newCommand ->
            currentFindOrgNavigationCommand = newCommand
        }.launchIn(viewModelScope)
    }

    private fun FindOrgByIdentifierDataModel.observeFindOrgDataFlow() {
        dataFlow.onEach { updatedState ->
            println("updatedState is $updatedState")
            findOrgState = updatedState
        }.launchIn(viewModelScope)
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        tfValue = ""
        currentFindOrgNavigationCommand = null
        findOrgState = PraxisDataModel.EmptyState
        onComplete()
    }
}