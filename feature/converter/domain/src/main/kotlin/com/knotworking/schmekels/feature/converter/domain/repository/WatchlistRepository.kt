package com.knotworking.schmekels.feature.converter.domain.repository

import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    val watchlist: Flow<Set<String>>
    suspend fun add(code: String)
    suspend fun remove(code: String)
}
