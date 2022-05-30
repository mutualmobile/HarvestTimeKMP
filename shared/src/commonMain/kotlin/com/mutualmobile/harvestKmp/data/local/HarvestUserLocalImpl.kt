package com.mutualmobile.harvestKmp.data.local

import com.mutualmobile.harvestKmp.db.BaseIoDB
import com.mutualmobile.harvestKmp.db.asFlow
import com.mutualmobile.harvestKmp.db.mapToList
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.squareup.sqldelight.db.SqlDriver
import db.Harvest_user
import kotlinx.coroutines.flow.Flow

class HarvestUserLocalImpl(override var driver: SqlDriver? = null) : HarvestUserLocal {

    private val database by lazy { BaseIoDB(driver!!) }
    private val dbQuery by lazy { database.harvestDBQueries }

    override fun saveUser(input: GetUserResponse) {
        dbQuery.insertUser(
            uid = input.id ?: throw RuntimeException("Woah! the uid should not be null"),
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            orgId = input.orgId,
            role = input.role
        )
    }

    override fun getAll(): Flow<List<Harvest_user>> {
        return dbQuery.selectAllUsers().asFlow().mapToList()
    }

    override fun getUser(): Harvest_user {
        return dbQuery.selectAllUsers().executeAsOne()
    }
}