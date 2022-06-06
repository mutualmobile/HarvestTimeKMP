package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.Routes
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GetUserDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val getUserUseCase = useCasesComponent.provideGetUserUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

    fun getUser() {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    print("GetUser Successful, ${getUserResponse.data}")
                    harvestLocal.saveUser(getUserResponse.data)
                    onDataState(SuccessState(getUserResponse.data))
                    praxisCommand(NavigationPraxisCommand(Routes.Screen.ORG_USER_DASHBOARD))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    onDataState(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                    onDataState(ErrorState(getUserResponse.throwable))
                }
            }
        }
    }

    override fun activate() {
        getUser()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }
}