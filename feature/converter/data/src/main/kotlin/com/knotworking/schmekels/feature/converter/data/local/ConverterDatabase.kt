package com.knotworking.schmekels.feature.converter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExchangeRateEntity::class, RateMetaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ConverterDatabase : RoomDatabase() {
    abstract val rateDao: RateDao
}
