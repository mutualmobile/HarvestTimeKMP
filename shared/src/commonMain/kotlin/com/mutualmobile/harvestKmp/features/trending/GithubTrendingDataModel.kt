package com.mutualmobile.harvestKmp.features.trending

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.UseCasesComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class GithubTrendingDataModel(
    private val onDataState: (DataState) -> Unit
) : PraxisDataModel(), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = UseCasesComponent()


    override fun activate() {
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
        dataModelScope.launch {
            useCasesComponent.provideGetLocalReposUseCase().perform(input = null)
                .collectLatest { list ->
                    onDataState(SuccessState(list))
                }
        }
    }

    fun filterRecords(search: String? = null) {
        fetchTrendingRepos(search)
    }

    private fun fetchTrendingRepos(search: String? = "kotlin") {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            val repos = useCasesComponent.provideFetchTrendingReposUseCase().perform(search)
            useCasesComponent.provideSaveTrendingReposUseCase().perform(repos)
        }
    }
}