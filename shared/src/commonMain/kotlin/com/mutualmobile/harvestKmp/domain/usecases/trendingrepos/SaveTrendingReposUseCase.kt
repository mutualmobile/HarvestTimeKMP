package com.mutualmobile.harvestKmp.domain.usecases.trendingrepos

import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocal
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import com.mutualmobile.harvestKmp.domain.usecases.BaseUseCase

class SaveTrendingReposUseCase(private val githubTrendingLocal: GithubTrendingLocal) :
    BaseUseCase<List<GithubReposItem>, Unit> {
    override suspend fun perform(input: List<GithubReposItem>?) {
        return githubTrendingLocal.saveRepos(input!!)
    }
}