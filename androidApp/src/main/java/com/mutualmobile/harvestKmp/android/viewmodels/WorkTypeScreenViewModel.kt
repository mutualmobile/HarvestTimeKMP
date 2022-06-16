package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WorkTypeScreenViewModel : ViewModel() {
    var isSearchBarVisible: Boolean by mutableStateOf(false)
    var searchQuery: String by mutableStateOf("")
    val workTypeList: List<Pair<String, String>> by mutableStateOf(listOf(
        Pair("BILLABLE", "Work"),
        Pair("NON-BILLABLE", "Non-Billable"),
    ))
}