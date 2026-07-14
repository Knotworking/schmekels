package com.knotworking.schmekels.feature.converter.domain

import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot

object CurrencyConverter {

    fun convert(
        amount: Double,
        from: String,
        to: String,
        snapshot: ExchangeRateSnapshot
    ): Double? {
        val fromRate = snapshot.rates[from] ?: return null
        val toRate = snapshot.rates[to] ?: return null
        return amount * (toRate / fromRate)
    }
}
