package com.knotworking.schmekels.feature.converter.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.knotworking.schmekels.feature.converter.domain.model.ExchangeRateSnapshot
import org.junit.jupiter.api.Test

class CurrencyConverterTest {

    private val snapshot = ExchangeRateSnapshot(
        base = "EUR",
        rates = mapOf("EUR" to 1.0, "GBP" to 0.85, "SEK" to 11.4),
        fetchedAt = 0L
    )

    @Test
    fun `convert applies cross rate between two non-base currencies`() {
        val result = CurrencyConverter.convert(amount = 10.0, from = "GBP", to = "SEK", snapshot = snapshot)

        assertThat(result).isEqualTo(10.0 * (11.4 / 0.85))
    }

    @Test
    fun `convert to same currency returns amount unchanged`() {
        val result = CurrencyConverter.convert(amount = 42.0, from = "EUR", to = "EUR", snapshot = snapshot)

        assertThat(result).isEqualTo(42.0)
    }

    @Test
    fun `convert returns null when from code is missing from rates`() {
        val result = CurrencyConverter.convert(amount = 10.0, from = "XXX", to = "EUR", snapshot = snapshot)

        assertThat(result).isNull()
    }

    @Test
    fun `convert returns null when to code is missing from rates`() {
        val result = CurrencyConverter.convert(amount = 10.0, from = "EUR", to = "XXX", snapshot = snapshot)

        assertThat(result).isNull()
    }
}
