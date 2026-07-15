package com.knotworking.schmekels.feature.converter.presentation.di

import com.knotworking.schmekels.feature.converter.presentation.converter.ConverterViewModel
import com.knotworking.schmekels.feature.converter.presentation.picker.CurrencyPickerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val converterPresentationModule = module {
    viewModelOf(::ConverterViewModel)
    viewModelOf(::CurrencyPickerViewModel)
}
