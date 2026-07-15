package com.knotworking.schmekels.feature.converter.presentation.fake

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import com.knotworking.schmekels.feature.converter.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeExchangeRateRepository(
    initialSnapshot: ExchangeRateSnapshot? = null
) : ExchangeRateRepository {

    private val cachedRates = MutableStateFlow(initialSnapshot)

    var getRatesResult: EmptyResult<DataError> = Result.Success(Unit)
    var getRatesCallCount = 0
        private set

    override suspend fun getRates(forceRefresh: Boolean): EmptyResult<DataError> {
        getRatesCallCount++
        return getRatesResult
    }

    override fun getCachedRates(): Flow<ExchangeRateSnapshot?> = cachedRates

    fun emitSnapshot(snapshot: ExchangeRateSnapshot?) {
        cachedRates.value = snapshot
    }
}
