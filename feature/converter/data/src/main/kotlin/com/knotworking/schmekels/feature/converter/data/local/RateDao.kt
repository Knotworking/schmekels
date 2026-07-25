package com.knotworking.schmekels.feature.converter.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Query("SELECT * FROM exchange_rates")
    fun observeRates(): Flow<List<ExchangeRateEntity>>

    @Query("SELECT * FROM rate_meta LIMIT 1")
    fun observeMeta(): Flow<RateMetaEntity?>

    @Query("DELETE FROM exchange_rates")
    suspend fun clearRates()

    @Upsert
    suspend fun upsertRates(rates: List<ExchangeRateEntity>)

    @Upsert
    suspend fun upsertMeta(meta: RateMetaEntity)

    @Transaction
    suspend fun replaceRates(rates: List<ExchangeRateEntity>, meta: RateMetaEntity) {
        Log.i("TAG", "replace rates locally: ${meta.fetchedAtEpoch}")
        clearRates()
        upsertRates(rates)
        upsertMeta(meta)
    }
}
