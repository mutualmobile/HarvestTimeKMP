package com.mutualmobile.harvestKmp.android.ui.utils

fun Number.toDecimalString(decimalPlaces: Int = 2) = "%.${decimalPlaces}f".format(this)