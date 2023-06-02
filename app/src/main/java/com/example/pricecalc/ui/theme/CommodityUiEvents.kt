package com.example.pricecalc.ui.theme

sealed class CommodityUiEvent{
    data class OnAddPrice(val msg : String):CommodityUiEvent()
    data class OnReducePrice(val msg : String):CommodityUiEvent()
    data class OnAddQuantity(val msg : String):CommodityUiEvent()
   data class OnReduceQuantity(val msg : String):CommodityUiEvent()
}
