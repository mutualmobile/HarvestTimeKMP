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
    private val findOrgByIdentifierDataModel = FindOrgByIdentifierDataModel()

    var tfValue by mutableStateOf("")

    var currentFindOrgNavigationCommand: PraxisCommand? by mutableStateOf(null)

    var findOrgState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    init {
        with(findOrgByIdentifierDataModel) {
            observeDataState()
            observeNavigationCommands()
            activate()
        }
    }

    private fun FindOrgByIdentifierDataModel.observeNavigationCommands() {
        praxisCommand.onEach { newCommand ->
            currentFindOrgNavigationCommand = newCommand
        }.launchIn(viewModelScope)
    }

    private fun FindOrgByIdentifierDataModel.observeDataState() {
        dataFlow.onEach { updatedState ->
            println("updatedState is $updatedState")
            findOrgState = updatedState
        }.launchIn(viewModelScope)
    }

    fun findOrgByIdentifier() {
        findOrgByIdentifierDataModel.findOrgByIdentifier(identifier = tfValue)
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        tfValue = ""
        currentFindOrgNavigationCommand = null
        findOrgState = PraxisDataModel.EmptyState
        onComplete()
    }
}