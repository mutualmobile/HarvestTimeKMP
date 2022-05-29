package com.mutualmobile.harvestKmp.data.local

import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.squareup.sqldelight.db.SqlDriver
import db.Harvest_user
import kotlinx.coroutines.flow.Flow

interface HarvestUserLocal {
    var driver: SqlDriver?
    fun saveUser(input: GetUserResponse)
    fun getAll(): Flow<List<Harvest_user>>
    fun getUser(): Harvest_user
}