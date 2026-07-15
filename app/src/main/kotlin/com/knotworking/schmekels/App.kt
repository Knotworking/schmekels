package com.knotworking.schmekels

import android.app.Application
import com.knotworking.schmekels.feature.converter.data.di.converterDataModule
import com.knotworking.schmekels.feature.converter.presentation.di.converterPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                converterDataModule,
                converterPresentationModule
            )
        }
    }
}
