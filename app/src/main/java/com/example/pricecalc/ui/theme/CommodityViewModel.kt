package com.example.pricecalc.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * stateflow means that all observers receive the same state of the ui at the same time
 */

private const val PRICE_INCREASE = 10.00
private const val QUANTITY_INCREASE = 1

class CommodityViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(CommodityUiState())
    val uiState: StateFlow<CommodityUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private fun updateTotalPrice() {
        _uiState.value = _uiState.value.copy(
            total = uiState.value.price.toInt().times(uiState.value.quantity).toString()
        )
    }

    fun handleUiEvent(event: CommodityUiEvent) {
        when (event) {
            is CommodityUiEvent.OnAddPrice -> {
                if (uiState.value.quantity.equals(0)){
                    _uiState.value = _uiState.value.copy(
                        quantity = QUANTITY_INCREASE
                    )
                }
                if(uiState.value.price >= 100){
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowToastMessage("Cant Add price >= 100"))
                    }
                    return
                }
                _uiState.value = _uiState.value.copy(
                    price = uiState.value.price.plus(PRICE_INCREASE)
                )
                updateTotalPrice()
            }

            is CommodityUiEvent.OnAddQuantity -> {
                if(uiState.value.quantity >= 10){
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowToastMessage("Cant Add quantity >= 10"))
                    }
                    return
                }
                _uiState.value = _uiState.value.copy(
                    quantity = uiState.value.quantity.plus(QUANTITY_INCREASE)
                )
                updateTotalPrice()
            }

            is CommodityUiEvent.OnReducePrice -> {
                if(uiState.value.price <= 0){
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowToastMessage("Cant Reduce price <= 0"))
                    }
                    return
                }
                _uiState.value = _uiState.value.copy(
                    price = uiState.value.price.minus(PRICE_INCREASE)
                )
                updateTotalPrice()
            }

            is CommodityUiEvent.OnReduceQuantity -> {
                if(uiState.value.quantity <= 0){
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowToastMessage("Cant Reduce quantity <= 0"))
                    }
                    return
                }
                _uiState.value = _uiState.value.copy(
                    quantity = uiState.value.quantity.minus(QUANTITY_INCREASE)
                )
                updateTotalPrice()
            }
        }
    }
}