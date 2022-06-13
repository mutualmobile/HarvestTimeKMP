package com.mutualmobile.harvestKmp.data.mappers

import com.mutualmobile.harvestKmp.domain.model.request.HarvestUserWorkRequest
import com.mutualmobile.harvestKmp.domain.model.response.HarvestUserWorkResponse

fun HarvestUserWorkResponse.toWorkRequest() = HarvestUserWorkRequest(
    id = id,
    projectId = projectId,
    userId = userId,
    workDate = workDate,
    workHours = workHours,
    note = note
)

fun HarvestUserWorkRequest.toWorkResponse() = HarvestUserWorkResponse(
    id = id,
    projectId = projectId,
    userId = userId,
    workDate = workDate,
    workHours = workHours,
    note = note
)
