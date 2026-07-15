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
        rates = mapOf("EUR" to 1.0, "GBP" to 0.5),
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
        exchangeRateRepository: FakeExchangeRateRepository = FakeExchangeRateRepository(snapshot)
    ): Pair<ConverterViewModel, FakeExchangeRateRepository> {
        val viewModel = ConverterViewModel(
            exchangeRateRepository = exchangeRateRepository,
            watchlistRepository = FakeWatchlistRepository(watchlist)
        )
        return viewModel to exchangeRateRepository
    }

    @Test
    fun `init builds rows from watchlist and cached snapshot`() = runTest {
        val (viewModel, _) = createViewModel()

        val codes = viewModel.state.value.rows.map { it.code }.toSet()
        assertThat(codes).isEqualTo(setOf("EUR", "GBP"))
    }

    @Test
    fun `amount change updates the active row immediately`() = runTest {
        val (viewModel, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))

        val eurRow = viewModel.state.value.rows.first { it.code == "EUR" }
        assertThat(eurRow.amount).isEqualTo("10")
    }

    @Test
    fun `amount change recomputes other rows after the debounce window`() = runTest {
        val (viewModel, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))
        advanceTimeBy(350)

        val gbpRow = viewModel.state.value.rows.first { it.code == "GBP" }
        assertThat(gbpRow.amount).isEqualTo("5")
    }

    @Test
    fun `amount change does not recompute other rows before the debounce window elapses`() = runTest {
        val (viewModel, _) = createViewModel()

        viewModel.onAction(ConverterAction.OnAmountChange(code = "EUR", amount = "10"))
        advanceTimeBy(100)

        val gbpRow = viewModel.state.value.rows.first { it.code == "GBP" }
        assertThat(gbpRow.amount).isEqualTo("1")
    }

    @Test
    fun `refresh failure surfaces error in state`() = runTest {
        val exchangeRateRepository = FakeExchangeRateRepository(snapshot).apply {
            getRatesResult = Result.Error(DataError.Network.NO_INTERNET)
        }

        val (viewModel, _) = createViewModel(exchangeRateRepository = exchangeRateRepository)

        assertThat(viewModel.state.value.error != null).isEqualTo(true)
    }
}
