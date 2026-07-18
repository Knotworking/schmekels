package com.knotworking.schmekels.feature.converter.presentation.converter

import androidx.compose.runtime.Stable
import com.knotworking.schmekels.core.presentation.UiText

@Stable
data class ConverterState(
    val rows: List<CurrencyRowUi> = emptyList(),
    val activeCode: String? = null,
    val ratesAsOf: String? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: UiText? = null
)

data class CurrencyRowUi(
    val code: String,
    val name: String,
    val symbol: String,
    val amount: String,
    val isDefault: Boolean = false
)
