package com.knotworking.schmekels.feature.converter.presentation.converter

sealed interface ConverterEvent {
    data object NavigateToPicker : ConverterEvent
}
