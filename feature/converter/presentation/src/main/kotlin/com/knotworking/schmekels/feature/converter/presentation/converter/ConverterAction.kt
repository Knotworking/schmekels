package com.knotworking.schmekels.feature.converter.presentation.converter

sealed interface ConverterAction {
    data class OnAmountChange(val code: String, val amount: String) : ConverterAction
    data class OnSetDefaultCurrency(val code: String) : ConverterAction
    data object OnRefreshClick : ConverterAction
    data object OnAddCurrencyClick : ConverterAction
}
