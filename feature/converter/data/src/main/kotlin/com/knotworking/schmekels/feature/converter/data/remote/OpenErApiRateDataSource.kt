package com.knotworking.schmekels.feature.converter.data.remote

import com.knotworking.schmekels.core.data.networking.get
import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.core.domain.util.map
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import io.ktor.client.HttpClient

class OpenErApiRateDataSource(
    private val httpClient: HttpClient
) : ExchangeRateRemoteDataSource {

    override suspend fun fetchLatestRates(): Result<ExchangeRateSnapshot, DataError.Network> {
        return httpClient.get<LatestRatesDto>(route = "/latest/EUR").map { it.toSnapshot() }
    }
}
