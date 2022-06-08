package com.mutualmobile.harvestKmp.android.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.android.ui.utils.get

@Composable
fun noAccountAnnotatedString() = buildAnnotatedString {
    append(MR.strings.dont_have_an_account.get())
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append(" ${MR.strings.try_harvest_free.get()}")
    }
}