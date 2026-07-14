package com.knotworking.schmekels.feature.converter.data.local

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.flow.Flow

interface ExchangeRateLocalDataSource {
    fun observeRates(): Flow<ExchangeRateSnapshot?>
    suspend fun saveRates(snapshot: ExchangeRateSnapshot): EmptyResult<DataError.Local>
}
