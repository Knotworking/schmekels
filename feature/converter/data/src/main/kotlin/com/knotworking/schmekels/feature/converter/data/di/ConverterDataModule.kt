package com.knotworking.schmekels.feature.converter.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.knotworking.schmekels.core.data.networking.HttpClientFactory
import com.knotworking.schmekels.feature.converter.data.local.ConverterDatabase
import com.knotworking.schmekels.feature.converter.data.local.ExchangeRateLocalDataSource
import com.knotworking.schmekels.feature.converter.data.local.RateDao
import com.knotworking.schmekels.feature.converter.data.local.RoomRateLocalDataSource
import com.knotworking.schmekels.feature.converter.data.remote.ExchangeRateRemoteDataSource
import com.knotworking.schmekels.feature.converter.data.remote.OpenErApiRateDataSource
import com.knotworking.schmekels.feature.converter.data.repository.DataStoreWatchlistRepository
import com.knotworking.schmekels.feature.converter.data.repository.OfflineFirstExchangeRateRepository
import com.knotworking.schmekels.feature.converter.domain.repository.ExchangeRateRepository
import com.knotworking.schmekels.feature.converter.domain.repository.WatchlistRepository
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val CONVERTER_DATASTORE_NAME = "converter_watchlist"
private const val CONVERTER_DATABASE_NAME = "converter.db"

val converterDataModule = module {
    single { HttpClientFactory.create(OkHttp.create()) }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile(CONVERTER_DATASTORE_NAME) }
        )
    }

    single {
        Room.databaseBuilder(androidContext(), ConverterDatabase::class.java, CONVERTER_DATABASE_NAME).build()
    }
    single<RateDao> { get<ConverterDatabase>().rateDao }

    singleOf(::OpenErApiRateDataSource) { bind<ExchangeRateRemoteDataSource>() }
    singleOf(::RoomRateLocalDataSource) { bind<ExchangeRateLocalDataSource>() }
    singleOf(::OfflineFirstExchangeRateRepository) { bind<ExchangeRateRepository>() }
    singleOf(::DataStoreWatchlistRepository) { bind<WatchlistRepository>() }
}
