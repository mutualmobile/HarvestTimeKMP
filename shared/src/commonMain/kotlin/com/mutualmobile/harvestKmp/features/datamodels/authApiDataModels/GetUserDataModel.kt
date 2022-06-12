package com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
import com.mutualmobile.harvestKmp.datamodel.ModalPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.NavigationPraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GetUserDataModel :
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
                    if (getUserResponse.data.role == UserRole.ORG_USER.role) {
                        harvestLocal.saveUser(getUserResponse.data)
                    }
                    _dataFlow.emit(SuccessState(getUserResponse.data))
                    praxisCommand(NavigationPraxisCommand(HarvestRoutes.Screen.ORG_USER_DASHBOARD))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    _dataFlow.emit(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                    _dataFlow.emit(ErrorState(getUserResponse.throwable))
                }
            }
        }
    }

    fun getLocalUser(): GetUserResponse? {
        return harvestLocal.getUser()?.let { dbUser ->
            GetUserResponse(
                email = dbUser.email,
                firstName = dbUser.firstName,
                id = dbUser.uid,
                lastName = dbUser.lastName,
                modifiedTime = null,
                orgId = dbUser.orgId,
                role = dbUser.role
            )
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