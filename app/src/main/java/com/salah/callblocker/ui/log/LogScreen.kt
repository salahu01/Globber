package com.salah.callblocker.ui.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salah.callblocker.data.BlockedCall
import com.salah.callblocker.data.PatternType
import com.salah.callblocker.ui.components.BentoCard
import com.salah.callblocker.ui.components.BentoVariant
import com.salah.callblocker.ui.components.CircleIconButton
import com.salah.callblocker.ui.components.ScreenHeader
import com.salah.callblocker.ui.icons.AppIcons
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

@Composable
fun LogScreen(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    val calls by viewModel.calls.collectAsStateWithLifecycle()
    val formatter = remember {
        SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault())
    }

    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Blocked Calls",
            onBack = onBack,
            action = {
                CircleIconButton(
                    icon = AppIcons.trash,
                    contentDescription = "Clear log",
                    onClick = { viewModel.clear() },
                    enabled = calls.isNotEmpty(),
                    outlined = true,
                    tint = MaterialTheme.colorScheme.error,
                )
            },
        )

        if (calls.isEmpty()) {
            EmptyLog(Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        BentoCard(variant = BentoVariant.Dark) {
            Text(
                text = "No blocked calls yet.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Blocked calls will show up here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun LogRow(call: BlockedCall, formatted: String) {
    BentoCard(variant = BentoVariant.Dark, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = call.number,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Matched \"${call.matchedPattern}\" (${call.matchedType.displayName()})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp),
        )
        Text(
            text = formatted,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
