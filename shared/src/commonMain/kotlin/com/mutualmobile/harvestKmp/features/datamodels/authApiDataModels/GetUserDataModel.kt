package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.ErrorState
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.LoadingState
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel.SuccessState
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class GetUserDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    private var currentLoadingJob: Job? = null
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val getUserUseCase = authApiUseCasesComponent.provideGetNetworkUserUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

    fun getUser(forceFetchFromNetwork: Boolean = false) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            _dataFlow.emit(LoadingState)
            if (!forceFetchFromNetwork) {
                harvestLocal.getUser()?.let { nnUser ->
                    _dataFlow.emit(
                        SuccessState(
                            GetUserResponse(
                                email = nnUser.email,
                                firstName = nnUser.firstName,
                                id = nnUser.uid,
                                lastName = nnUser.lastName,
                                modifiedTime = null,
                                orgId = nnUser.orgId,
                                role = nnUser.role
                            )
                        )
                    )
                    return@launch
                }
            }
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    print("GetUser Successful, ${getUserResponse.data}")
                    harvestLocal.saveUser(getUserResponse.data)
                    _dataFlow.emit(SuccessState(getUserResponse.data))
                    intPraxisCommand.emit(NavigationPraxisCommand(HarvestRoutes.Screen.ORG_USER_DASHBOARD))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    _dataFlow.emit(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    intPraxisCommand.emit(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    intPraxisCommand.emit(NavigationPraxisCommand(""))
                    _dataFlow.emit(ErrorState(getUserResponse.throwable))
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