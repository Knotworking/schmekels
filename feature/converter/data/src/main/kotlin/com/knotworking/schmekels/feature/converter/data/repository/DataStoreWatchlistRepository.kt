package com.knotworking.schmekels.feature.converter.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.knotworking.schmekels.feature.converter.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val WATCHLIST_KEY = stringSetPreferencesKey("watchlist")
private val DEFAULT_WATCHLIST = setOf("EUR", "GBP", "SEK")

class DataStoreWatchlistRepository(
    private val dataStore: DataStore<Preferences>
) : WatchlistRepository {

    override val watchlist: Flow<Set<String>>
        get() = dataStore.data.map { prefs -> prefs[WATCHLIST_KEY] ?: DEFAULT_WATCHLIST }

    override suspend fun add(code: String) {
        dataStore.edit { prefs ->
            prefs[WATCHLIST_KEY] = (prefs[WATCHLIST_KEY] ?: DEFAULT_WATCHLIST) + code
        }
    }

    override suspend fun remove(code: String) {
        dataStore.edit { prefs ->
            prefs[WATCHLIST_KEY] = (prefs[WATCHLIST_KEY] ?: DEFAULT_WATCHLIST) - code
        }
    }
}
