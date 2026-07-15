package com.knotworking.schmekels.feature.converter.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.knotworking.schmekels.feature.converter.presentation.converter.ConverterRoot
import com.knotworking.schmekels.feature.converter.presentation.picker.CurrencyPickerRoot

fun NavGraphBuilder.converterGraph(navController: NavController) {
    composable<ConverterRoute> {
        ConverterRoot(
            onNavigateToPicker = { navController.navigate(PickerRoute) }
        )
    }
    composable<PickerRoute> {
        CurrencyPickerRoot(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
