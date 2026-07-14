package com.knotworking.schmekels.feature.converter.data.repository

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.data.local.ExchangeRateLocalDataSource
import com.knotworking.schmekels.feature.converter.data.remote.ExchangeRateRemoteDataSource
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import com.knotworking.schmekels.feature.converter.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

private val STALENESS_THRESHOLD_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(1)

class OfflineFirstExchangeRateRepository(
    private val remoteDataSource: ExchangeRateRemoteDataSource,
    private val localDataSource: ExchangeRateLocalDataSource
) : ExchangeRateRepository {

    override suspend fun getRates(forceRefresh: Boolean): EmptyResult<DataError> {
        val cached = localDataSource.observeRates().firstOrNull()
        val isStale = cached == null || System.currentTimeMillis() - cached.fetchedAt > STALENESS_THRESHOLD_MS

        if (!forceRefresh && !isStale) {
            return Result.Success(Unit)
        }

        return when (val remoteResult = remoteDataSource.fetchLatestRates()) {
            is Result.Success -> localDataSource.saveRates(remoteResult.data)
            is Result.Error -> if (cached != null) Result.Success(Unit) else remoteResult
        }
    }

    override fun getCachedRates(): Flow<ExchangeRateSnapshot?> {
        return localDataSource.observeRates()
    }
}
