package com.knotworking.schmekels.feature.converter.domain.model

data class ExchangeRateSnapshot(
    val base: String,
    val rates: Map<String, Double>,
    val fetchedAt: Long
)
