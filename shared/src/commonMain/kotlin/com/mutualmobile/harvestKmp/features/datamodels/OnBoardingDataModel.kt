package com.mutualmobile.harvestKmp.features.datamodels

import com.mutualmobile.harvestKmp.domain.model.OnBoardingItem
import com.mutualmobile.harvestKmp.domain.model.onBoardingItem
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.PraxisDataModel
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent

class OnBoardingDataModel(onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

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