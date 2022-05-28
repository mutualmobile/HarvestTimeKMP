package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GetUserDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val getUserUseCase = useCasesComponent.provideGetUserUseCase()
//    val settings = Settings()
    val settings = SharedComponent().provideSettings()

    fun getUser() {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val getUserResponse = getUserUseCase()) {
                is NetworkResponse.Success -> {
                    print("GetUser Successful, ${getUserResponse.data.message}")
                    onDataState(SuccessState(getUserResponse.data))

                    /*---Trying to save the userId and OrgId for use in the api calls---*/
//                    settings["USER_ID"] = getUserResponse.data.data?.id
//                    settings["ORG_ID"] = getUserResponse.data.data?.orgId

                }
                is NetworkResponse.Failure -> {
                    print("GetUser Failed, ${getUserResponse.throwable.message}")
                    onDataState(ErrorState(getUserResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }

    /*---Trying to save the userId and OrgId for use in the api calls---*/

//    private fun saveIdsAndNavigate(getUserResponse: NetworkResponse.Success<GetUserResponse>) {
//        getUserResponse.data.id?.let { userId ->
//            getUserResponse.data.orgId?.let { orgId ->
//                saveUserDetailsUseCase(
//                    userId,
//                    orgId
//                )
//            }
//        }
//    }

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }
}