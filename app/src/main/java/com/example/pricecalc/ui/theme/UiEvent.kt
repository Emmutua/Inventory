package com.example.pricecalc.ui.theme

sealed class UiEvent{
    data class ShowToastMessage(val msg : String): UiEvent()
}

