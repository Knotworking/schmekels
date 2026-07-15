package com.knotworking.schmekels.feature.converter.data.fake

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.data.local.ExchangeRateLocalDataSource
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeExchangeRateLocalDataSource(
    initialSnapshot: ExchangeRateSnapshot? = null
) : ExchangeRateLocalDataSource {

    private val snapshot = MutableStateFlow(initialSnapshot)

    var saveCallCount = 0
        private set

    override fun observeRates(): Flow<ExchangeRateSnapshot?> = snapshot

    override suspend fun saveRates(snapshot: ExchangeRateSnapshot): EmptyResult<DataError.Local> {
        saveCallCount++
        this.snapshot.value = snapshot
        return Result.Success(Unit)
    }
}
