package com.knotworking.schmekels.feature.converter.presentation.picker

sealed interface CurrencyPickerAction {
    data class OnQueryChange(val query: String) : CurrencyPickerAction
    data class OnToggleCurrency(val code: String) : CurrencyPickerAction
}
