package com.mutualmobile.harvestKmp.features.datamodels

import com.mutualmobile.harvestKmp.domain.model.OnBoardingItem
import com.mutualmobile.harvestKmp.domain.model.onBoardingItem
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent

class OnBoardingDataModel() :
    PraxisDataModel(), KoinComponent {
  private val _dataFlow = MutableSharedFlow<DataState>()
    val dataFlow = _dataFlow.asSharedFlow()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()

    }

    override fun refresh() {
    }

    fun getOnBoardingItemList(): List<OnBoardingItem> {
        return onBoardingItem
    }

}