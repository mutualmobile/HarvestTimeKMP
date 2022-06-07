package com.mutualmobile.harvestKmp.features.datamodels.userProjectDataModels

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.UserProjectUseCaseComponent
import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.features.NetworkResponse
import db.Harvest_user
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LogWorkTimeDataModel(private val onDataState: (DataState) -> Unit = {}) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val userProjectUseCaseComponent = UserProjectUseCaseComponent()
    private val logWorkTimeUseCase =
        userProjectUseCaseComponent.provideLogWorkTimeUseCase()
    private val getUserAssignedProjectsUseCase =
        userProjectUseCaseComponent.provideGetUserAssignedProjectsUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

    override fun activate() {

    }

    fun getUser(): Harvest_user? {
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
                    praxisCommand(ModalPraxisCommand("Success", response.data.message ?: ""))
                }
                is NetworkResponse.Failure -> {
                    this.emit(ErrorState(response.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
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
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}
