package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest

enum class WorkRequestType {
    CREATE, UPDATE
}

class NewEntryScreenViewModel : ViewModel() {
    var currentWorkRequest: HarvestUserWorkRequest? by mutableStateOf(null)
        private set

    var currentProjectName: String by mutableStateOf("")
        private set

    var currentWorkRequestType by mutableStateOf(WorkRequestType.CREATE)
        private set

    fun updateCurrentWorkRequest(
        update: (existing: HarvestUserWorkRequest?) -> HarvestUserWorkRequest?,
        onUpdateCompleted: () -> Unit = {}
    ) {
        currentWorkRequest = update(currentWorkRequest)
        onUpdateCompleted()
    }

    fun updateCurrentProjectName(projectName: String, onUpdateCompleted: () -> Unit = {}) {
        currentProjectName = projectName
        onUpdateCompleted()
    }

    fun updateCurrentWorkRequestType(workRequestType: WorkRequestType, onUpdateCompleted: () -> Unit = {}) {
        currentWorkRequestType = workRequestType
        onUpdateCompleted()
    }

    // TODO: Implement this
    fun fetchProjectName(projectId: String = "") {
        currentProjectName = "Loading..."
    }

    fun resetAllItems(onResetCompleted: () -> Unit = {}) {
        currentWorkRequest = null
        currentProjectName = ""
        currentWorkRequestType = WorkRequestType.CREATE
        onResetCompleted()
    }
}