package com.fegno.callblocker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fegno.callblocker.ui.icons.AppIcons
import com.fegno.callblocker.ui.theme.LocalCallBlockerColors

enum class BentoVariant { Accent, Dark }

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    variant: BentoVariant = BentoVariant.Dark,
    onClick: (() -> Unit)? = null,
    contentPadding: Int = 20,
    content: @Composable ColumnScope.() -> Unit,
) {
    val accents = LocalCallBlockerColors.current
    val shape = MaterialTheme.shapes.large
    val base = when (variant) {
        BentoVariant.Accent -> Modifier.clip(shape).background(accents.accentFill)
        BentoVariant.Dark -> Modifier.glassSurface(shape)
    }
    Column(
        modifier = modifier
            .then(base)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding.dp),
        content = content,
    )
}

@Composable
private fun ArrowButton(onClick: () -> Unit, onAccent: Boolean) {
    val accents = LocalCallBlockerColors.current
    val fg = if (onAccent) accents.onAccent else MaterialTheme.colorScheme.onSurface
    val border = if (onAccent) accents.onAccent.copy(alpha = 0.35f)
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f)
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(BorderStroke(1.dp, border), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            AppIcons.arrowRight,
            contentDescription = "Open",
            tint = fg,
            modifier = Modifier.size(18.dp),
        )
    }
}

/** Big hero stat: large number + unit suffix + optional delta + optional mini-track. */
@Composable
fun StatHeroCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    unit: String? = null,
    subtitle: String? = null,
    delta: DeltaInfo? = null,
    track: Float? = null,
    onArrowClick: (() -> Unit)? = null,
) {
    val accents = LocalCallBlockerColors.current
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(accents.accentFill)
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = accents.onAccent,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = accents.onAccent.copy(alpha = 0.7f),
                    )
                }
            }
            if (onArrowClick != null) ArrowButton(onArrowClick, onAccent = true)
        }
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge,
                color = accents.onAccent,
                fontWeight = FontWeight.Light,
            )
            if (unit != null) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelLarge,
                    color = accents.onAccent.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 6.dp, bottom = 14.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            if (delta != null) {
                DeltaText(delta, modifier = Modifier.padding(bottom = 14.dp))
            }
        }
        if (track != null) {
            Spacer(Modifier.height(14.dp))
            MiniTrack(
                progress = track,
                modifier = Modifier.fillMaxWidth(),
                trackColor = accents.onAccent.copy(alpha = 0.2f),
                fillColor = accents.onAccent,
            )
        }
    }
}

/** Smaller bento stat tile. */
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    variant: BentoVariant = BentoVariant.Dark,
    delta: DeltaInfo? = null,
    track: Float? = null,
    onArrowClick: (() -> Unit)? = null,
) {
    val accents = LocalCallBlockerColors.current
    val onAccent = variant == BentoVariant.Accent
    val labelColor = if (onAccent) accents.onAccent.copy(alpha = 0.8f)
    else MaterialTheme.colorScheme.onSurfaceVariant
    val valueColor = if (onAccent) accents.onAccent else MaterialTheme.colorScheme.onSurface
    BentoCard(modifier = modifier, variant = variant) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = labelColor,
            )
            if (onArrowClick != null) ArrowButton(onArrowClick, onAccent = onAccent)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.displayMedium,
            color = valueColor,
            fontWeight = FontWeight.Light,
            maxLines = 1,
        )
        if (sub != null || delta != null) {
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (sub != null) {
                    Text(
                        text = sub,
                        style = MaterialTheme.typography.bodyMedium,
                        color = labelColor,
                    )
                }
                if (delta != null) {
                    Spacer(Modifier.weight(1f))
                    DeltaText(delta)
                }
            }
        }
        if (track != null) {
            Spacer(Modifier.height(12.dp))
            MiniTrack(progress = track, modifier = Modifier.fillMaxWidth())
        }
    }
}
