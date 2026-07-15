package com.knotworking.schmekels.feature.converter.presentation.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotworking.schmekels.feature.converter.domain.model.CurrencyCatalog
import com.knotworking.schmekels.feature.converter.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrencyPickerViewModel(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val state: StateFlow<CurrencyPickerState> = combine(
        query,
        watchlistRepository.watchlist
    ) { query, watchlist ->
        CurrencyPickerState(
            query = query,
            results = CurrencyCatalog.all
                .filter { currency ->
                    query.isBlank() ||
                        currency.code.contains(query, ignoreCase = true) ||
                        currency.name.contains(query, ignoreCase = true)
                }
                .map { currency ->
                    CurrencyPickerItemUi(
                        code = currency.code,
                        name = currency.name,
                        symbol = currency.symbol,
                        isSelected = currency.code in watchlist
                    )
                }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CurrencyPickerState())

    fun onAction(action: CurrencyPickerAction) {
        when (action) {
            is CurrencyPickerAction.OnQueryChange -> query.value = action.query
            is CurrencyPickerAction.OnToggleCurrency -> toggleCurrency(action.code)
        }
    }

    private fun toggleCurrency(code: String) {
        val isSelected = state.value.results.find { it.code == code }?.isSelected ?: false
        viewModelScope.launch {
            if (isSelected) watchlistRepository.remove(code) else watchlistRepository.add(code)
        }
    }
}
