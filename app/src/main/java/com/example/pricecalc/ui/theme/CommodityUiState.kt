package com.example.pricecalc.ui.theme

data class CommodityUiState(
    val name: String = "",
    val price: Double = 0.0,
    val quantity : Int = 0,
    val total : String = "0.0",
    val msg: String = ""
)
