package com.example.pricecalc.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val quantity = _uiState.value.quantity
    private val price = _uiState.value.price

    private fun updateTotalPrice() {
        _uiState.value = _uiState.value.copy(
            total = price.toInt().times(quantity).toString()
        )
    }

    fun handleUiEvent(event: CommodityUiEvent) {
        when (event) {
            is CommodityUiEvent.OnAddPrice -> {
//                if (price > 100) {
//                    viewModelScope.launch {
//                        _uiEvent.send(
//                            UiEvent.ShowToastMessage("Price > 100")
//                        )
//                    }
//                    return
//                }
//                _uiState.update {
//                    _uiState.value.copy(
//                        price = price.plus(PRICE_INCREASE)
//                    )
//                }
                _uiState.value = _uiState.value.copy(
                    price = price.plus(PRICE_INCREASE)
                )
                updateTotalPrice()
                UiEvent.ShowToastMessage(event.msg)
            }

            is CommodityUiEvent.OnAddQuantity -> {
                if (quantity > 10) {
                    viewModelScope.launch {
                        UiEvent.ShowToastMessage("Cant Add")
                    }
                    return
                }
                _uiState.update {
                    _uiState.value.copy(
                        quantity = quantity + QUANTITY_INCREASE
                    )
                }
                    UiEvent.ShowToastMessage(event.msg)
                    updateTotalPrice()
            }

            is CommodityUiEvent.OnReducePrice -> {
                if (price <= 0) {
                    viewModelScope.launch {
                        _uiEvent.send(
                            UiEvent.ShowToastMessage("Price <= 0")
                        )
                    }
                    return
                }
                _uiState.update {
                    _uiState.value.copy(
                        price = price - PRICE_INCREASE
                    )
                }
                    updateTotalPrice()
                    UiEvent.ShowToastMessage(event.msg)
            }

            is CommodityUiEvent.OnReduceQuantity -> {
                if (quantity < 0) {
                    viewModelScope.launch {
                        UiEvent.ShowToastMessage("Cant Reduce")
                    }
                    return
                }
                _uiState.update {
                    _uiState.value.copy(
                        quantity = quantity - QUANTITY_INCREASE
                    )
                }
                    UiEvent.ShowToastMessage(event.msg)
                    updateTotalPrice()
            }
        }
    }
}