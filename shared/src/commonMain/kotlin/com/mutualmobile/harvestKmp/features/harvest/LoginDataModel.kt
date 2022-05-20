package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.LoadingState
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class LoginDataModel(onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()


    fun login(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            dataState.value = LoadingState
            val loginResponse = useCasesComponent.provideLoginUseCase()
                .perform(email, password)
            when (loginResponse) {
                is NetworkResponse.Success -> {
                    dataState.value =
                        SuccessState(loginResponse.data)
                }
                is NetworkResponse.Failure -> {
                    dataState.value =
                        ErrorState(loginResponse.exception)
                }
            }
        }
    }



    override fun activate() {
        listenState()
    }

    override fun destroy() {

    }

    override fun refresh() {

    }


    data class SuccessState(
        val trendingList: LoginResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}
