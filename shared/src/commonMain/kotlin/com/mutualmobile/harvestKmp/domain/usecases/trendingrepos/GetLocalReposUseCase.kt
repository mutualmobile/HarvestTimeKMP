package com.mutualmobile.harvestKmp.domain.usecases.trendingrepos

import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocal
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import com.mutualmobile.harvestKmp.domain.usecases.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLocalReposUseCase(private val githubTrendingLocal: GithubTrendingLocal) :
    BaseUseCase<Unit, Flow<List<GithubReposItem>>> {
    override suspend fun perform(input: Unit?): Flow<List<GithubReposItem>> {
        return githubTrendingLocal.getAll().map {
            it.map {
                GithubReposItem(it.author, it.avatar, it.description, it.language, it.name, it.url)
            }
        }
    }
}