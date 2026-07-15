package com.knotworking.schmekels.feature.converter.presentation.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotworking.schmekels.core.domain.util.onFailure
import com.knotworking.schmekels.core.presentation.util.toUiText
import com.knotworking.schmekels.feature.converter.domain.CurrencyConverter
import com.knotworking.schmekels.feature.converter.domain.model.CurrencyCatalog
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import com.knotworking.schmekels.feature.converter.domain.repository.ExchangeRateRepository
import com.knotworking.schmekels.feature.converter.domain.repository.WatchlistRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private const val DEFAULT_AMOUNT = "1"
private const val RECOMPUTE_DEBOUNCE_MS = 300L

@OptIn(FlowPreview::class)
class ConverterViewModel(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConverterState())
    val state = _state.asStateFlow()

    private val _events = Channel<ConverterEvent>()
    val events = _events.receiveAsFlow()

    private var latestSnapshot: ExchangeRateSnapshot? = null
    private val amountChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        observeWatchlistAndRates()
        observeAmountChanges()
        refresh(forceRefresh = false)
    }

    fun onAction(action: ConverterAction) {
        when (action) {
            is ConverterAction.OnAmountChange -> onAmountChange(action.code, action.amount)
            ConverterAction.OnRefreshClick -> refresh(forceRefresh = true)
            ConverterAction.OnAddCurrencyClick -> navigateToPicker()
        }
    }

    private fun observeWatchlistAndRates() {
        combine(
            watchlistRepository.watchlist,
            exchangeRateRepository.getCachedRates()
        ) { codes, snapshot -> codes to snapshot }
            .onEach { (codes, snapshot) ->
                latestSnapshot = snapshot
                _state.update { current ->
                    current.copy(
                        rows = buildRows(codes, current),
                        ratesAsOf = snapshot?.fetchedAt?.let(::formatFetchedAt)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun buildRows(codes: Set<String>, current: ConverterState): List<CurrencyRowUi> {
        val existing = current.rows.associateBy { it.code }
        return codes.sorted().mapNotNull { code ->
            val currency = CurrencyCatalog[code] ?: return@mapNotNull null
            CurrencyRowUi(
                code = code,
                name = currency.name,
                symbol = currency.symbol,
                amount = existing[code]?.amount ?: DEFAULT_AMOUNT
            )
        }
    }

    private fun onAmountChange(code: String, amount: String) {
        _state.update { current ->
            current.copy(
                activeCode = code,
                rows = current.rows.map { row -> if (row.code == code) row.copy(amount = amount) else row }
            )
        }
        amountChanges.tryEmit(Unit)
    }

    private fun observeAmountChanges() {
        amountChanges
            .debounce(RECOMPUTE_DEBOUNCE_MS)
            .onEach { recomputeInactiveRows() }
            .launchIn(viewModelScope)
    }

    private fun recomputeInactiveRows() {
        val snapshot = latestSnapshot ?: return
        val current = _state.value
        val activeCode = current.activeCode ?: return
        val activeRow = current.rows.find { it.code == activeCode } ?: return
        val activeAmount = activeRow.amount.toDoubleOrNull() ?: return

        _state.update { state ->
            state.copy(
                rows = state.rows.map { row ->
                    if (row.code == activeCode) {
                        row
                    } else {
                        val converted = CurrencyConverter.convert(activeAmount, activeCode, row.code, snapshot)
                        converted?.let { row.copy(amount = formatAmount(it)) } ?: row
                    }
                }
            )
        }
    }

    private fun refresh(forceRefresh: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = forceRefresh) }
            exchangeRateRepository.getRates(forceRefresh)
                .onFailure { error -> _state.update { it.copy(error = error.toUiText()) } }
            _state.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    private fun navigateToPicker() {
        viewModelScope.launch { _events.send(ConverterEvent.NavigateToPicker) }
    }
}

private fun formatAmount(value: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 4
    formatter.isGroupingUsed = true
    return formatter.format(value)
}

private fun formatFetchedAt(epochMillis: Long): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(formatter)
}
