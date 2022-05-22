package com.mutualmobile.harvestKmp.domain.usecases.trendingrepos

import com.mutualmobile.harvestKmp.data.network.GithubTrendingAPI
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import com.mutualmobile.harvestKmp.domain.usecases.BaseUseCase

class FetchTrendingReposUseCase(private val githubTrendingAPI: GithubTrendingAPI) :
    BaseUseCase<String, List<GithubReposItem>> {
    override suspend fun perform(input: String?): List<GithubReposItem> {
        return githubTrendingAPI.getTrendingRepos(input!!)
    }
}