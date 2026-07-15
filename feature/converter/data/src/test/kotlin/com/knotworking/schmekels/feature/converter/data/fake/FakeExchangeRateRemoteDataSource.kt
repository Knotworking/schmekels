package com.knotworking.schmekels.feature.converter.data.fake

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.data.remote.ExchangeRateRemoteDataSource
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot

class FakeExchangeRateRemoteDataSource(
    var result: Result<ExchangeRateSnapshot, DataError.Network> = Result.Success(
        ExchangeRateSnapshot(base = "EUR", rates = mapOf("EUR" to 1.0), fetchedAt = 0L)
    )
) : ExchangeRateRemoteDataSource {

    var fetchCallCount = 0
        private set

    override suspend fun fetchLatestRates(): Result<ExchangeRateSnapshot, DataError.Network> {
        fetchCallCount++
        return result
    }
}
