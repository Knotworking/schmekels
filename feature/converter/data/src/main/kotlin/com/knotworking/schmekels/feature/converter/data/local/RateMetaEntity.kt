package com.knotworking.schmekels.feature.converter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rate_meta")
data class RateMetaEntity(
    @PrimaryKey val base: String,
    val fetchedAtEpoch: Long
)
