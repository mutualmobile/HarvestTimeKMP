package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.GithubReposItem

interface GithubTrendingAPI {
    suspend fun getTrendingRepos(
        query: String
    ): List<GithubReposItem>
}