package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
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

    fun getUser() {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    print("GetUser Successful, ${getUserResponse.data.message}")
                    onDataState(SuccessState(getUserResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.exception.message}")
                    onDataState(ErrorState(getUserResponse.exception))
                }
            }
        }
    }

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }
}