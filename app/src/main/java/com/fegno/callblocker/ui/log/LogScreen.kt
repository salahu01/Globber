package com.fegno.callblocker.ui.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fegno.callblocker.data.BlockedCall
import com.fegno.callblocker.data.PatternType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun PatternType.displayName(): String = when (this) {
    PatternType.EXACT -> "Exact"
    PatternType.STARTS_WITH -> "Starts with"
    PatternType.CONTAINS -> "Contains"
    PatternType.ENDS_WITH -> "Ends with"
    PatternType.REGEX -> "Regex"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = viewModel(),
) {
    val calls by viewModel.calls.collectAsStateWithLifecycle()
    val formatter = remember {
        SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault())
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Blocked Calls") },
                actions = {
                    IconButton(
                        onClick = { viewModel.clear() },
                        enabled = calls.isNotEmpty(),
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Clear log")
                    }
                },
            )
        },
    ) { innerPadding ->
        if (calls.isEmpty()) {
            EmptyLog(Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(calls, key = { it.id }) { call ->
                    LogRow(call = call, formatted = formatter.format(Date(call.timestamp)))
                }
            }
        }
    }
}

@Composable
private fun EmptyLog(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No blocked calls yet.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LogRow(call: BlockedCall, formatted: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = call.number,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = "Matched \"${call.matchedPattern}\" (${call.matchedType.displayName()})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatted,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
