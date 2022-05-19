package com.baseio.kmm.features.harvest

import com.baseio.kmm.datamodel.PraxisDataModel
import com.baseio.kmm.di.UseCasesComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class LoginDataModel : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = UseCasesComponent()

    private val _loginState: MutableStateFlow<LoginDataModel.DataState> = MutableStateFlow(
        LoginDataModel.EmptyState
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _loginState.value = LoginDataModel.ErrorState(throwable)
    }


    override fun activate() {

    }

    override fun destroy() {

    }

    override fun refresh() {

    }

    sealed class DataState
    object LoadingState : DataState()
    object EmptyState : DataState()
    object Complete : DataState()
    data class SuccessState(
        val loginResponse: LoginResponse,
    ) : DataState()

    data class ErrorState(var throwable: Throwable) : DataState()
}
