package com.knotworking.schmekels.feature.converter.presentation.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.knotworking.schmekels.core.designsystem.theme.SchmekelsTheme
import com.knotworking.schmekels.feature.converter.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CurrencyPickerRoot(
    onNavigateBack: () -> Unit,
    viewModel: CurrencyPickerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CurrencyPickerScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerScreen(
    state: CurrencyPickerState,
    onAction: (CurrencyPickerAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.picker_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { onAction(CurrencyPickerAction.OnQueryChange(it)) },
                label = { Text(stringResource(R.string.picker_search_placeholder)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = state.results, key = { it.code }) { item ->
                    CurrencyPickerRow(
                        item = item,
                        onToggle = { onAction(CurrencyPickerAction.OnToggleCurrency(item.code)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyPickerRow(
    item: CurrencyPickerItemUi,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Checkbox(checked = item.isSelected, onCheckedChange = { onToggle() })
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "${item.code} · ${item.symbol}", style = MaterialTheme.typography.bodyLarge)
            Text(text = item.name, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
private fun CurrencyPickerScreenPreview() {
    SchmekelsTheme {
        CurrencyPickerScreen(
            state = CurrencyPickerState(
                query = "",
                results = listOf(
                    CurrencyPickerItemUi("EUR", "Euro", "€", isSelected = true),
                    CurrencyPickerItemUi("GBP", "British Pound Sterling", "£", isSelected = true),
                    CurrencyPickerItemUi("USD", "United States Dollar", "$", isSelected = false)
                )
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
