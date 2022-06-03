package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException

class ProjectCrudValidator {
    operator fun invoke(name: String, client: String) {
        if (name.isEmpty()) {
            throw EmptyFieldException("Name")
        }
        if (client.isEmpty()) {
            throw EmptyFieldException("Client Name")
        }
    }
}