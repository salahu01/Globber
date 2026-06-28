package com.fegno.callblocker.ui.rules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fegno.callblocker.data.BlockRule
import com.fegno.callblocker.data.PatternType
import com.fegno.callblocker.data.RuleAction
import com.fegno.callblocker.ui.components.BentoCard
import com.fegno.callblocker.ui.components.BentoVariant
import com.fegno.callblocker.ui.components.CircleIconButton
import com.fegno.callblocker.ui.components.PillButton
import com.fegno.callblocker.ui.components.ScreenHeader
import com.fegno.callblocker.ui.icons.AppIcons
import com.fegno.callblocker.ui.theme.LocalCallBlockerColors

fun PatternType.displayName(): String = when (this) {
    PatternType.EXACT -> "Exact"
    PatternType.STARTS_WITH -> "Starts with"
    PatternType.CONTAINS -> "Contains"
    PatternType.ENDS_WITH -> "Ends with"
    PatternType.REGEX -> "Regex"
}

fun RuleAction.displayName(): String = when (this) {
    RuleAction.REJECT -> "Reject"
    RuleAction.SILENCE -> "Silence"
    RuleAction.VOICEMAIL -> "Voicemail"
}

@Composable
fun RulesScreen(
    modifier: Modifier = Modifier,
    viewModel: RulesViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    val rules by viewModel.rules.collectAsStateWithLifecycle()
    val accents = LocalCallBlockerColors.current
    // null = no dialog; non-null wrapper holds the rule being edited (or null inside = add)
    var editorState by remember { mutableStateOf<EditorState?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = { ScreenHeader(title = "Rules", onBack = onBack) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editorState = EditorState(null) },
                containerColor = accents.accentFill,
                contentColor = accents.onAccent,
                shape = RoundedCornerShape(50),
                icon = { Icon(AppIcons.plus, contentDescription = null) },
                text = { Text("Add rule") },
            )
        },
    ) { innerPadding ->
        if (rules.isEmpty()) {
            EmptyRules(
                onAdd = { editorState = EditorState(null) },
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 88.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(rules, key = { it.id }) { rule ->
                    RuleRow(
                        rule = rule,
                        onToggle = { viewModel.toggle(rule) },
                        onDelete = { viewModel.delete(rule) },
                        onMoveUp = { viewModel.moveUp(rule) },
                        onMoveDown = { viewModel.moveDown(rule) },
                        onClick = { editorState = EditorState(rule) },
                    )
                }
            }
        }
    }

    editorState?.let { state ->
        RuleEditorDialog(
            initial = state.rule,
            onDismiss = { editorState = null },
            onConfirm = { pattern, type, action, label ->
                val initial = state.rule
                if (initial == null) {
                    viewModel.addRule(pattern, type, label, action)
                } else {
                    viewModel.updateRule(
                        initial.copy(
                            pattern = pattern,
                            type = type,
                            label = label,
                            action = action,
                        )
                    )
                }
                editorState = null
            },
        )
    }
}

private class EditorState(val rule: BlockRule?)

@Composable
private fun EmptyRules(onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        BentoCard(variant = BentoVariant.Dark) {
            Text(
                text = "No blocking rules yet.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Add a number or pattern to start blocking calls.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
            )
            PillButton(text = "Add your first rule", onClick = onAdd)
        }
    }
}

@Composable
private fun TagPill(text: String) {
    val accents = LocalCallBlockerColors.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, accents.accentFill, RoundedCornerShape(50))
            .background(accents.accentFill.copy(alpha = 0.10f))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun RuleRow(
    rule: BlockRule,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onClick: () -> Unit,
) {
    val accents = LocalCallBlockerColors.current
    BentoCard(
        variant = BentoVariant.Dark,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = 16,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rule.label.ifBlank { rule.pattern },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = rule.pattern,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                TagPill("${rule.type.displayName()} · ${rule.action.displayName()}")
            }
            Switch(
                checked = rule.enabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = accents.accentFill,
                    checkedThumbColor = accents.onAccent,
                ),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CircleIconButton(
                icon = AppIcons.chevronUp,
                contentDescription = "Move up",
                onClick = onMoveUp,
                outlined = true,
            )
            CircleIconButton(
                icon = AppIcons.chevronDown,
                contentDescription = "Move down",
                onClick = onMoveDown,
                outlined = true,
            )
            Box(modifier = Modifier.weight(1f))
            CircleIconButton(
                icon = AppIcons.trash,
                contentDescription = "Delete rule",
                onClick = onDelete,
                outlined = true,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RuleEditorDialog(
    initial: BlockRule?,
    onDismiss: () -> Unit,
    onConfirm: (pattern: String, type: PatternType, action: RuleAction, label: String) -> Unit,
) {
    var pattern by remember { mutableStateOf(initial?.pattern ?: "") }
    var label by remember { mutableStateOf(initial?.label ?: "") }
    var type by remember { mutableStateOf(initial?.type ?: PatternType.EXACT) }
    var action by remember { mutableStateOf(initial?.action ?: RuleAction.REJECT) }
    var typeExpanded by remember { mutableStateOf(false) }
    var actionExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.large,
        title = { Text(if (initial == null) "Add rule" else "Edit rule") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = pattern,
                    onValueChange = { pattern = it },
                    label = { Text("Pattern") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                ) {
                    OutlinedTextField(
                        value = type.displayName(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Match type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    DropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                    ) {
                        PatternType.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName()) },
                                onClick = {
                                    type = option
                                    typeExpanded = false
                                },
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = actionExpanded,
                    onExpandedChange = { actionExpanded = it },
                ) {
                    OutlinedTextField(
                        value = action.displayName(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Action") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = actionExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    DropdownMenu(
                        expanded = actionExpanded,
                        onDismissRequest = { actionExpanded = false },
                    ) {
                        RuleAction.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName()) },
                                onClick = {
                                    action = option
                                    actionExpanded = false
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            PillButton(
                text = if (initial == null) "Add" else "Save",
                onClick = { onConfirm(pattern.trim(), type, action, label.trim()) },
                enabled = pattern.isNotBlank(),
            )
        },
        dismissButton = {
            PillButton(text = "Cancel", onClick = onDismiss, filled = false)
        },
    )
}
