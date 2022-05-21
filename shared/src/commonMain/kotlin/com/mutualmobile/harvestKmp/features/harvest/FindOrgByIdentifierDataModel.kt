package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.FindOrgResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel(onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    fun findOrgByIdentifier(identifier: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            dataState.value = LoadingState

            val response = useCasesComponent.provideFindOrgByIdentifier()
                .perform(identifier)
            when (response) {
                is NetworkResponse.Success -> {
                    dataState.value = SuccessState(response.data)
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    dataState.value = ErrorState(response.exception)
                    println("FAILED, ${response.exception.message}")
                }
            }
        }
    }

    override fun activate() {
        listenState()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

}