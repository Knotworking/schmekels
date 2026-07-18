package com.knotworking.schmekels.feature.converter.presentation.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.knotworking.schmekels.core.designsystem.components.LoadingIndicator
import com.knotworking.schmekels.core.designsystem.theme.SchmekelsTheme
import com.knotworking.schmekels.core.presentation.ObserveAsEvents
import com.knotworking.schmekels.feature.converter.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConverterRoot(
    onNavigateToPicker: () -> Unit,
    viewModel: ConverterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ConverterEvent.NavigateToPicker -> onNavigateToPicker()
        }
    }

    ConverterScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    state: ConverterState,
    onAction: (ConverterAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.converter_title)) },
                actions = {
                    if (state.isRefreshing) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        TextButton(onClick = { onAction(ConverterAction.OnRefreshClick) }) {
                            Text(stringResource(R.string.action_refresh))
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(ConverterAction.OnAddCurrencyClick) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add_currency)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            state.ratesAsOf?.let { ratesAsOf ->
                Text(
                    text = stringResource(R.string.rates_as_of, ratesAsOf),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            state.error?.let { error ->
                Text(
                    text = error.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            if (state.isLoading) {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = state.rows, key = { it.code }) { row ->
                        CurrencyRow(
                            row = row,
                            onAmountChange = {
                                onAction(
                                    ConverterAction.OnAmountChange(
                                        row.code,
                                        it
                                    )
                                )
                            },
                            onSetDefaultClick = { onAction(ConverterAction.OnSetDefaultCurrency(row.code)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyRow(
    row: CurrencyRowUi,
    onAmountChange: (String) -> Unit,
    onSetDefaultClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onSetDefaultClick) {
            Icon(
                imageVector = if (row.isDefault) Icons.Filled.Home else Icons.Outlined.Home,
                tint = if (row.isDefault) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                contentDescription = if (row.isDefault) {
                    stringResource(R.string.cd_default_currency, row.code)
                } else {
                    stringResource(R.string.cd_set_default_currency, row.code)
                }
            )
        }
        OutlinedTextField(
            value = row.amount,
            onValueChange = onAmountChange,
            label = { Text("${row.code} · ${row.name}") },
            leadingIcon = { Text(row.symbol) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview
@Composable
private fun ConverterScreenPreview() {
    SchmekelsTheme {
        ConverterScreen(
            state = ConverterState(
                rows = listOf(
                    CurrencyRowUi("EUR", "Euro", "€", "1", isDefault = true),
                    CurrencyRowUi("GBP", "British Pound Sterling", "£", "0.86"),
                    CurrencyRowUi("SEK", "Swedish Krona", "kr", "11.42")
                ),
                ratesAsOf = "Jul 15, 2026, 2:32:00 PM",
                isLoading = false
            ),
            onAction = {}
        )
    }
}
