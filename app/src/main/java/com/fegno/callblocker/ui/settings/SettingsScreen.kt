package com.fegno.callblocker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    vm: SettingsViewModel,
    onExport: () -> Unit,
    onImport: () -> Unit,
    onEnableContactsAllowlist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val allowContacts by vm.allowContacts.collectAsStateWithLifecycle()
    val notifyOnBlock by vm.notifyOnBlock.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SettingSwitchRow(
            title = "Allow calls from contacts",
            subtitle = "Calls from people saved in your contacts are never blocked.",
            checked = allowContacts,
            onCheckedChange = { enabled ->
                if (enabled) {
                    onEnableContactsAllowlist()
                    vm.setAllowContacts(true)
                } else {
                    vm.setAllowContacts(false)
                }
            },
        )

        HorizontalDivider()

        SettingSwitchRow(
            title = "Notify when a call is blocked",
            subtitle = "Show a notification each time a call is blocked.",
            checked = notifyOnBlock,
            onCheckedChange = { vm.setNotifyOnBlock(it) },
        )

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                onClick = onExport,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Export rules")
            }
            Text(
                text = "Save all your rules to a JSON file.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                onClick = onImport,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Import rules")
            }
            Text(
                text = "Add rules from a previously exported JSON file.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
