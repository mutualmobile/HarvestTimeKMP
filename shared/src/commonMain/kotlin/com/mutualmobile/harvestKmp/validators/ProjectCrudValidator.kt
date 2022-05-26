package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException

class ProjectCrudValidator {
    operator fun invoke(name: String, client: String, startDate: String, endDate: String) {
        if (name.isEmpty()) {
            throw EmptyFieldException("Name")
        }
        if (client.isEmpty()) {
            throw EmptyFieldException("Client Name")
        }
        if (startDate.isEmpty()) {
            throw EmptyFieldException("Start Date")
        }
        if (endDate.isEmpty()) {
            throw EmptyFieldException("End Date")
        }
    }
}