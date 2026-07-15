package com.knotworking.schmekels.feature.converter.data.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.data.fake.FakeExchangeRateLocalDataSource
import com.knotworking.schmekels.feature.converter.data.fake.FakeExchangeRateRemoteDataSource
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class OfflineFirstExchangeRateRepositoryTest {

    private val freshSnapshot = ExchangeRateSnapshot(
        base = "EUR",
        rates = mapOf("EUR" to 1.0),
        fetchedAt = System.currentTimeMillis()
    )

    private val staleSnapshot = ExchangeRateSnapshot(
        base = "EUR",
        rates = mapOf("EUR" to 1.0),
        fetchedAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)
    )

    @Test
    fun `fresh cache skips remote fetch when refresh is not forced`() = runTest {
        val remote = FakeExchangeRateRemoteDataSource()
        val local = FakeExchangeRateLocalDataSource(freshSnapshot)
        val repository = OfflineFirstExchangeRateRepository(remote, local)

        val result = repository.getRates(forceRefresh = false)

        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat(remote.fetchCallCount).isEqualTo(0)
    }

    @Test
    fun `stale cache triggers a remote fetch and saves the result`() = runTest {
        val remote = FakeExchangeRateRemoteDataSource()
        val local = FakeExchangeRateLocalDataSource(staleSnapshot)
        val repository = OfflineFirstExchangeRateRepository(remote, local)

        val result = repository.getRates(forceRefresh = false)

        assertThat(result).isInstanceOf(Result.Success::class)
        assertThat(remote.fetchCallCount).isEqualTo(1)
        assertThat(local.saveCallCount).isEqualTo(1)
    }

    @Test
    fun `forceRefresh bypasses a fresh cache and hits the remote`() = runTest {
        val remote = FakeExchangeRateRemoteDataSource()
        val local = FakeExchangeRateLocalDataSource(freshSnapshot)
        val repository = OfflineFirstExchangeRateRepository(remote, local)

        repository.getRates(forceRefresh = true)

        assertThat(remote.fetchCallCount).isEqualTo(1)
    }

    @Test
    fun `remote failure with no cache propagates the error`() = runTest {
        val remote = FakeExchangeRateRemoteDataSource(result = Result.Error(DataError.Network.NO_INTERNET))
        val local = FakeExchangeRateLocalDataSource(initialSnapshot = null)
        val repository = OfflineFirstExchangeRateRepository(remote, local)

        val result = repository.getRates(forceRefresh = false)

        assertThat(result).isInstanceOf(Result.Error::class)
    }

    @Test
    fun `remote failure with an existing cache falls back to success`() = runTest {
        val remote = FakeExchangeRateRemoteDataSource(result = Result.Error(DataError.Network.NO_INTERNET))
        val local = FakeExchangeRateLocalDataSource(staleSnapshot)
        val repository = OfflineFirstExchangeRateRepository(remote, local)

        val result = repository.getRates(forceRefresh = false)

        assertThat(result).isInstanceOf(Result.Success::class)
    }
}
