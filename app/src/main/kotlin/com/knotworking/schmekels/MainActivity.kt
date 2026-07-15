package com.knotworking.schmekels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.knotworking.schmekels.core.designsystem.theme.SchmekelsTheme
import com.knotworking.schmekels.feature.converter.presentation.navigation.ConverterRoute
import com.knotworking.schmekels.feature.converter.presentation.navigation.converterGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchmekelsTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ConverterRoute
                ) {
                    converterGraph(navController)
                }
            }
        }
    }
}
