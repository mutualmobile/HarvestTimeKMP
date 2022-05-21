package com.mutualmobile.harvestKmp.features.trending

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GithubTrendingDataModel(
    onDataState: (DataState) -> Unit
) : PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = UseCasesComponent()


    override fun activate() {
        listenState()
        readLocalRepositories()
        fetchTrendingRepos()
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
        fetchTrendingRepos()
    }

    private fun readLocalRepositories() {
        dataModelScope.launch(exceptionHandler) {
            useCasesComponent.provideGetLocalReposUseCase().perform(input = null)
                .collectLatest { list ->
                    dataState.value = SuccessState(list)
                }
        }
    }

    fun filterRecords(search: String? = null) {
        fetchTrendingRepos(search)
    }

    private fun fetchTrendingRepos(search: String? = "kotlin") {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch(exceptionHandler) {
            dataState.value = LoadingState
            val repos = useCasesComponent.provideFetchTrendingReposUseCase().perform(search)
            useCasesComponent.provideSaveTrendingReposUseCase().perform(repos)
        }
    }
}