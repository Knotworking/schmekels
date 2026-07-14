package com.knotworking.schmekels.feature.converter.domain.repository

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    suspend fun getRates(forceRefresh: Boolean): EmptyResult<DataError>
    fun getCachedRates(): Flow<ExchangeRateSnapshot?>
}
