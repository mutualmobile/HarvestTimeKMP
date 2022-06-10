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
import org.koin.core.component.KoinComponent

class GetUserDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val authApiUseCasesComponent = AuthApiUseCaseComponent()
    private val getUserUseCase = authApiUseCasesComponent.provideGetNetworkUserUseCase()
    private val harvestLocal = SharedComponent().provideHarvestUserLocal()

    fun getUser(forceFetchFromNetwork: Boolean = false) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            if (!forceFetchFromNetwork) {
                harvestLocal.getUser()?.let { nnUser ->
                    onDataState(
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
                    onDataState(SuccessState(getUserResponse.data))
                    praxisCommand(NavigationPraxisCommand(HarvestRoutes.Screen.ORG_USER_DASHBOARD))
                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    onDataState(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized", "Please login again!"))
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