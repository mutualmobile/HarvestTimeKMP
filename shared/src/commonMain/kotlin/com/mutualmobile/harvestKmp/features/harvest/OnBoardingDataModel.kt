package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.components.OnBoardingItem
import com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.components.onBoardingItem
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class OnBoardingDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> get() = _currentPage

    fun setCurrentPage(currentPage: Int) {
        _currentPage.value = currentPage
    }
    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()

    }

    override fun refresh() {
    }

    fun getOnBoardingItemList():List<OnBoardingItem>{
        return onBoardingItem;
    }

}