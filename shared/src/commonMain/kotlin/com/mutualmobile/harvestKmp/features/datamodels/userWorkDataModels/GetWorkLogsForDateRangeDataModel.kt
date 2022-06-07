package com.mutualmobile.harvestKmp.features.datamodels.userWorkDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.UserWorkUseCaseComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GetWorkLogsForDateRangeDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val userWorkUseCaseComponent = UserWorkUseCaseComponent()
    private val getWorkLogsForDateRangeUseCase =
        userWorkUseCaseComponent.provideGetWorkLogsForDateRangeUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun getWorkLogsForDateRange(
        startDate: String,
        endDate: String,
        userIds: List<String>?
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val response =
                getWorkLogsForDateRangeUseCase(
                    startDate = startDate,
                    endDate = endDate,
                    userIds = userIds
                )) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(response.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}