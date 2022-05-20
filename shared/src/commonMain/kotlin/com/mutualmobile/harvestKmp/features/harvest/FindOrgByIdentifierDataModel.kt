package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.FindOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FindOrgByIdentifierDataModel() : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    private val _findOrgByIdentifierStateFlow: MutableStateFlow<DataState> = MutableStateFlow(EmptyState)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _findOrgByIdentifierStateFlow.value = LoginDataModel.ErrorState(throwable)
    }

    fun FindOrgByIdentifier(identifier: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            _findOrgByIdentifierStateFlow.value = LoadingState

            val response = useCasesComponent.provideFindOrgByIdentifier()
                .perform(identifier)
            when (response) {
                is NetworkResponse.Success -> {
                    _findOrgByIdentifierStateFlow.value = SuccessState(response.data)
                    println("SUCCESS, ${response.data.message}")
                }
                is NetworkResponse.Failure -> {
                    _findOrgByIdentifierStateFlow.value = ErrorState(response.exception)
                    println("FAILED, ${response.exception.message}")
                }
            }
        }
    }

    override fun activate() {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    data class SuccessState(
        val List: FindOrgResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()

}