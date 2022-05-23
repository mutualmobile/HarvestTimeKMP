package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class LoginDataModel(private val onDataState: (DataState) -> Unit) : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val loginUseCase = useCasesComponent.provideLoginUseCase()

    // Todo Inject Settings
    private val settings = Settings()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun login(email: String, password: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val loginResponse = loginUseCase.perform(email, password)) {
                is NetworkResponse.Success -> {
                    print(loginResponse.data)
                    settings["JWT_TOKEN"] =loginResponse.data.token
                    settings["REFRESH_TOKEN"] = loginResponse.data.refreshToken
                    onDataState(SuccessState(loginResponse.data))
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(loginResponse.exception))
                }
            }
        }
    }
}
