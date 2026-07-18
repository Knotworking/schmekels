package com.knotworking.schmekels.feature.converter.presentation.converter

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import com.knotworking.schmekels.feature.converter.presentation.fake.FakeExchangeRateRepository
import com.knotworking.schmekels.feature.converter.presentation.fake.FakeWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class ConverterViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val snapshot = ExchangeRateSnapshot(
        base = "EUR",
        rates = mapOf("EUR" to 1.0, "GBP" to 0.5, "SEK" to 10.0),
        fetchedAt = 0L
    )

    @BeforeEach
    fun setUp() {
        Locale.setDefault(Locale.US)
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(
        watchlist: Set<String> = setOf("EUR", "GBP"),
        exchangeRateRepository: FakeExchangeRateRepository = FakeExchangeRateRepository(snapshot),
        watchlistRepository: FakeWatchlistRepository = FakeWatchlistRepository(watchlist)
    ): Triple<ConverterViewModel, FakeExchangeRateRepository, FakeWatchlistRepository> {
        val viewModel = ConverterViewModel(
            exchangeRateRepository = exchangeRateRepository,
            watchlistRepository = watchlistRepository
        )
        return Triple(viewModel, exchangeRateRepository, watchlistRepository)
    }

    @Test
    fun `init builds rows from watchlist and cached snapshot with default currency at 1`() = runTest {
        val (viewModel, _, _) = createViewModel()

        val rows = viewModel.state.value.rows.associateBy { it.code }
        assertThat(rows.keys).isEqualTo(setOf("EUR", "GBP"))
        assertThat(rows.getValue("EUR").amount).isEqualTo("1")
        assertThat(rows.getValue("EUR").isDefault).isEqualTo(true)
        assertThat(rows.getValue("GBP").amount).isEqualTo("0.5")
        assertThat(rows.getValue("GBP").isDefault).isEqualTo(false)
    }

    @Test
    fun `first launch uses persisted default currency as base`() = runTest {
        val watchlistRepository = FakeWatchlistRepository(
            initial = setOf("EUR", "GBP"),
            initialDefaultCurrency = "GBP"
        )

        val (viewModel, _, _) = createViewModel(watchlistRepository = watchlistRepository)

        val rows = viewModel.state.value.rows.associateBy { it.code }
        assertThat(rows.getValue("GBP").amount).isEqualTo("1")
        assertThat(rows.getValue("GBP").isDefault).isEqualTo(true)
        assertThat(rows.getValue("EUR").amount).isEqualTo("2")
    }

    @Test
    fun `adding a currency to the watchlist computes its converted amount immediately`() = runTest {
        val watchlistRepository = FakeWatchlistRepository(setOf("EUR", "GBP"))
        val (viewModel, _, _) = createViewModel(watchlistRepository = watchlistRepository)

        watchlistRepository.add("SEK")

        val sekRow = viewModel.state.value.rows.first { it.code == "SEK" }
        assertThat(sekRow.amount).isEqualTo("10")
    }

    @Test
    fun `amount change updates the active row immediately`() = runTest {
        val (viewModel, _, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))

        val eurRow = viewModel.state.value.rows.first { it.code == "EUR" }
        assertThat(eurRow.amount).isEqualTo("10")
    }

    @Test
    fun `amount change recomputes other rows after the debounce window`() = runTest {
        val (viewModel, _, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))
        advanceTimeBy(350)

        val gbpRow = viewModel.state.value.rows.first { it.code == "GBP" }
        assertThat(gbpRow.amount).isEqualTo("5")
    }

    @Test
    fun `amount change does not recompute other rows before the debounce window elapses`() = runTest {
        val (viewModel, _, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))
        advanceTimeBy(100)

        val gbpRow = viewModel.state.value.rows.first { it.code == "GBP" }
        assertThat(gbpRow.amount).isEqualTo("0.5")
    }

    @Test
    fun `setting default currency resets it to 1 and recomputes other rows`() = runTest {
        val watchlistRepository = FakeWatchlistRepository(setOf("EUR", "GBP"))
        val (viewModel, _, _) = createViewModel(watchlistRepository = watchlistRepository)

        viewModel.onAction(ConverterAction.OnSetDefaultCurrency("GBP"))
        advanceTimeBy(350)

        val rows = viewModel.state.value.rows.associateBy { it.code }
        assertThat(rows.getValue("GBP").amount).isEqualTo("1")
        assertThat(rows.getValue("GBP").isDefault).isEqualTo(true)
        assertThat(rows.getValue("EUR").amount).isEqualTo("2")
        assertThat(rows.getValue("EUR").isDefault).isEqualTo(false)
        assertThat(watchlistRepository.setDefaultCurrencyCallCount).isEqualTo(1)
    }

    @Test
    fun `refresh failure surfaces error in state`() = runTest {
        val exchangeRateRepository = FakeExchangeRateRepository(snapshot).apply {
            getRatesResult = Result.Error(DataError.Network.NO_INTERNET)
        }

        val (viewModel, _, _) = createViewModel(exchangeRateRepository = exchangeRateRepository)

        assertThat(viewModel.state.value.error != null).isEqualTo(true)
    }
}
