package com.mutualmobile.harvestKmp.android.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.icerock.moko.resources.StringResource

fun Number.toDecimalString(decimalPlaces: Int = 2) = "%.${decimalPlaces}f".format(this)

@Composable
fun StringResource.get() = stringResource(id = resourceId)