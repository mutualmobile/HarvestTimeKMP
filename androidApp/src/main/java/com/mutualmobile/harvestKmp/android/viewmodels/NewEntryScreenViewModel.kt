package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.android.ui.screens.newEntryScreen.components.serverDateFormatter
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.OrgProjectsDataModel
import com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels.TimeLogginDataModel
import java.util.Date
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

    var durationEtText: String by mutableStateOf(
        currentWorkRequest?.workHours?.toDecimalString() ?: ""
    )

    var selectedWorkDate: Date by mutableStateOf(currentWorkRequest?.workDate?.let { nnWorkDate ->
        serverDateFormatter.parse(nnWorkDate)
    } ?: Date())

    var noteEtText: String by mutableStateOf(currentWorkRequest?.note.orEmpty())

    var currentLogWorkTimeState: PraxisDataModel.DataState by mutableStateOf(
        PraxisDataModel.EmptyState
    )

    var logWorkTimeNavigationCommands: PraxisCommand? by mutableStateOf(null)

    val logWorkTimeDataModel = TimeLogginDataModel().apply {
        praxisCommand.onEach { newCommand ->
            logWorkTimeNavigationCommands = newCommand
        }.launchIn(dataModelScope)
    }

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

    fun fetchProjectName(projectId: String) {
        currentProjectName = "Loading..."
        viewModelScope.launch {
            OrgProjectsDataModel().apply {
                getProjectsForProjectIds(projectIds = listOf(projectId)).collect { newState ->
                    when (newState) {
                        is SuccessState<*> -> {
                            (newState as SuccessState<ApiResponse<List<OrgProjectResponse>>>)
                                .data.data?.let { nnOrgList ->
                                    nnOrgList.find { org -> org.id == projectId }?.let { nnOrg ->
                                        currentProjectName = nnOrg.name.orEmpty()
                                    }
                                }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    fun resetAllItems(onResetCompleted: () -> Unit = {}) {
        currentWorkRequest = null
        currentProjectName = ""
        currentWorkRequestType = WorkRequestType.CREATE
        onResetCompleted()
    }
}