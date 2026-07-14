package com.knotworking.schmekels.feature.converter.data.local

import com.knotworking.schmekels.core.domain.util.DataError
import com.knotworking.schmekels.core.domain.util.EmptyResult
import com.knotworking.schmekels.core.domain.util.Result
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RoomRateLocalDataSource(
    private val dao: RateDao
) : ExchangeRateLocalDataSource {

    override fun observeRates(): Flow<ExchangeRateSnapshot?> {
        return combine(dao.observeRates(), dao.observeMeta()) { rates, meta ->
            if (meta == null || rates.isEmpty()) {
                null
            } else {
                ExchangeRateSnapshot(
                    base = meta.base,
                    rates = rates.associate { it.code to it.rate },
                    fetchedAt = meta.fetchedAtEpoch
                )
            }
        }
    }

    override suspend fun saveRates(snapshot: ExchangeRateSnapshot): EmptyResult<DataError.Local> {
        return try {
            dao.replaceRates(
                rates = snapshot.rates.map { (code, rate) -> ExchangeRateEntity(code, rate) },
                meta = RateMetaEntity(base = snapshot.base, fetchedAtEpoch = snapshot.fetchedAt)
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
