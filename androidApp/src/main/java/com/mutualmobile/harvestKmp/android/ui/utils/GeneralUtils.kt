package com.mutualmobile.harvestKmp.android.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.icerock.moko.resources.StringResource

fun Number.toDecimalString(decimalPlaces: Int = 2) = "%.${decimalPlaces}f".format(this)

fun String.isNotAFloat(): Boolean = this.toFloatOrNull() == null
fun String.isAFloat():Boolean = isNotAFloat().not()

@Composable
fun StringResource.get() = stringResource(id = resourceId)

fun String.dateWithoutTimeZone() = substring(0, 10)

fun Context.showToast(
    msg: String, isLongToast: Boolean = true
) {
    Toast.makeText(
        this,
        msg,
        if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}