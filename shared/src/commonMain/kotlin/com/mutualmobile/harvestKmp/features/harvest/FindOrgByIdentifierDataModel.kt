package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    fun findOrgByIdentifier(identifier: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch() {
            onDataState(LoadingState)

            val response = useCasesComponent.provideFindOrgByIdentifier()
                .perform(identifier)
            when (response) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(response.data))
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(response.exception))
                    println("FAILED, ${response.exception.message}")
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