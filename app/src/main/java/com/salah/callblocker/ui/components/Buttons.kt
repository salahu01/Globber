package com.salah.callblocker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.salah.callblocker.ui.theme.LocalCallBlockerColors

@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    filled: Boolean = true,
) {
    val accents = LocalCallBlockerColors.current
    val shape = RoundedCornerShape(50)
    val container = if (filled) accents.accentFill else MaterialTheme.colorScheme.surfaceContainerHighest
    val content = if (filled) accents.onAccent else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier
            .clip(shape)
            .then(
                if (!filled) Modifier.border(
                    BorderStroke(1.dp, accents.accentFill),
                    shape,
                ) else Modifier
            )
            .background(if (filled) container else MaterialTheme.colorScheme.surfaceContainerHighest)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = content,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 0.dp),
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = content,
            modifier = Modifier.padding(start = if (leadingIcon != null) 8.dp else 0.dp),
        )
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    filled: Boolean = false,
    outlined: Boolean = false,
    onAccent: Boolean = false,
    tint: androidx.compose.ui.graphics.Color? = null,
) {
    val accents = LocalCallBlockerColors.current
    val shape = CircleShape
    val container = when {
        filled -> accents.accentFill
        outlined -> androidx.compose.ui.graphics.Color.Transparent
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val content = tint ?: when {
        filled -> accents.onAccent
        onAccent -> accents.onAccent
        else -> MaterialTheme.colorScheme.onSurface
    }
    val borderColor = if (onAccent) accents.onAccent.copy(alpha = 0.35f)
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f)
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(shape)
            .background(container)
            .then(if (outlined) Modifier.border(BorderStroke(1.dp, borderColor), shape) else Modifier)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = content,
            modifier = Modifier.size(20.dp),
        )
    }
}
