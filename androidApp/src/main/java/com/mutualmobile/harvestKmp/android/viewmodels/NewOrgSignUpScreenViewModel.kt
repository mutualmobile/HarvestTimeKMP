package com.mutualmobile.harvestKmp.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutualmobile.harvestKmp.datamodel.PraxisCommand
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.SignUpDataModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewOrgSignUpScreenViewModel : ViewModel() {
    var currentPraxisCommand: PraxisCommand? by mutableStateOf(null)

    var signUpState: PraxisDataModel.DataState by mutableStateOf(PraxisDataModel.EmptyState)

    val signUpDataModel = SignUpDataModel()

    var currentWorkEmail by mutableStateOf("")
    var currentPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var currentFirstName by mutableStateOf("")
    var currentLastName by mutableStateOf("")
    var companyName by mutableStateOf("")
    var companyWebsite by mutableStateOf("")
    var companyIdentifier by mutableStateOf("")

    init {
        with(signUpDataModel) {
            observeDataState()
            praxisCommand.onEach { newCommand ->
                currentPraxisCommand = newCommand
            }.launchIn(viewModelScope)
        }
    }

    private fun SignUpDataModel.observeDataState() {
        dataFlow.onEach { newState ->
            signUpState = newState
        }.launchIn(viewModelScope)
    }

    fun resetAll(onComplete: () -> Unit) {
        currentPraxisCommand = null
        signUpState = PraxisDataModel.EmptyState
        currentWorkEmail = ""
        currentPassword = ""
        confirmPassword = ""
        currentFirstName = ""
        currentLastName = ""
        companyName = ""
        companyWebsite = ""
        companyIdentifier = ""
        onComplete()
    }
}