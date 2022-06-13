package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.datamodels.orgProjectsDataModels.OrgProjectsDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TimeScreenViewModel : ViewModel() {
    private val orgProjectsDataModel = OrgProjectsDataModel()

    var getProjectsState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)
        private set
    var currentWeekWorkLogsOrgList by mutableStateOf(emptyList<OrgProjectResponse>())

    fun getProjects(projectIds: List<String>) {
        orgProjectsDataModel.getProjectsForProjectIds(projectIds = projectIds).onEach { newState ->
            getProjectsState = newState
            when (newState) {
                is PraxisDataModel.SuccessState<*> -> {
                    (newState.data as? ApiResponse<List<OrgProjectResponse>>)?.data?.let { nnList ->
                        currentWeekWorkLogsOrgList = nnList
                    }
                }
                else -> Unit
            }
        }.launchIn(orgProjectsDataModel.dataModelScope)
    }
}