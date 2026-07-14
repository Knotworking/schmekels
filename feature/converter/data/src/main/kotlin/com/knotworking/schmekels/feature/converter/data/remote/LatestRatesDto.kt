package com.knotworking.schmekels.feature.converter.data.remote

import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestRatesDto(
    @SerialName("base_code") val baseCode: String,
    @SerialName("time_last_update_unix") val timeLastUpdateUnix: Long,
    val rates: Map<String, Double>
)

fun LatestRatesDto.toSnapshot(): ExchangeRateSnapshot = ExchangeRateSnapshot(
    base = baseCode,
    rates = rates,
    fetchedAt = timeLastUpdateUnix * 1000L
)
