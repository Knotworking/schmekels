package com.knotworking.schmekels.feature.converter.data.remote

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot

interface ExchangeRateRemoteDataSource {
    suspend fun fetchLatestRates(): Result<ExchangeRateSnapshot, DataError.Network>
}
