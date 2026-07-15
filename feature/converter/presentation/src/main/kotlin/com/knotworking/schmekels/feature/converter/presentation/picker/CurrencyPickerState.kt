package com.knotworking.schmekels.feature.converter.presentation.picker

import androidx.compose.runtime.Stable

@Stable
data class CurrencyPickerState(
    val query: String = "",
    val results: List<CurrencyPickerItemUi> = emptyList()
)

data class CurrencyPickerItemUi(
    val code: String,
    val name: String,
    val symbol: String,
    val isSelected: Boolean
)
