package com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.UserProjectUseCaseComponent
import com.mutualmobile.harvestKmp.di.UserWorkUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import db.Harvest_user
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class TimeLogginDataModel() :
    PraxisDataModel(), KoinComponent {

    private val userProjectUseCaseComponent = UserProjectUseCaseComponent()
    private val logWorkTimeUseCase =
        userProjectUseCaseComponent.provideLogWorkTimeUseCase()
    private val deleteWorkTimeUseCase = userProjectUseCaseComponent.provideDeleteWorkTimeUseCase()
    private val getUserAssignedProjectsUseCase =
        userProjectUseCaseComponent.provideGetUserAssignedProjectsUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

    private val userWorkUseCaseComponent = UserWorkUseCaseComponent()
    private val getWorkLogsForDateRangeUseCase =
        userWorkUseCaseComponent.provideGetWorkLogsForDateRangeUseCase()

    val userId by lazy { getUser()?.uid }

    override fun activate() {

    }

    private fun getUser(): Harvest_user? {
        return harvestLocal.getUser()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun logWorkTime(
        harvestUserWorkRequest: HarvestUserWorkRequest
    ): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val response =
                logWorkTimeUseCase(
                    harvestUserWorkRequest
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(response.data))
                    intPraxisCommand.emit(ModalPraxisCommand("Success", response.data.message ?: ""))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun getUserAssignedProjects(
        userId: String?
    ): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val response =
                getUserAssignedProjectsUseCase(
                    userId = userId
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(response.data))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun getWorkLogsForDateRange(
        startDate: String,
        endDate: String,
        userIds: List<String>?
    ): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val response =
                getWorkLogsForDateRangeUseCase(
                    startDate = startDate,
                    endDate = endDate,
                    userIds = userIds
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(response.data))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }
    }

    fun deleteWork(harvestUserWorkResponse: HarvestUserWorkResponse): Flow<DataState> {
        return flow {
            this.emit(LoadingState)
            when (val response =
                deleteWorkTimeUseCase(
                    harvestUserWorkResponse
                )) {
                is NetworkResponse.Success -> {
                    this.emit(SuccessState(response.data))
                    intPraxisCommand.emit(ModalPraxisCommand("Success", response.data.message ?: ""))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                }
            }
        }
    }
}
