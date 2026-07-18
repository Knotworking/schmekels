package com.knotworking.schmekels.feature.converter.domain.repository

import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    val watchlist: Flow<Set<String>>
    val defaultCurrency: Flow<String>
    suspend fun add(code: String)
    suspend fun remove(code: String)
    suspend fun setDefaultCurrency(code: String)
}
