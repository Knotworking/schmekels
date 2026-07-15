package com.knotworking.schmekels.feature.converter.presentation.fake

import com.knotworking.schmekels.feature.converter.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWatchlistRepository(
    initial: Set<String> = emptySet()
) : WatchlistRepository {

    private val _watchlist = MutableStateFlow(initial)
    override val watchlist: Flow<Set<String>> = _watchlist

    override suspend fun add(code: String) {
        _watchlist.update { it + code }
    }

    override suspend fun remove(code: String) {
        _watchlist.update { it - code }
    }
}
