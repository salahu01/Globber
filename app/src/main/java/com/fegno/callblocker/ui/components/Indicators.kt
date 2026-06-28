package com.fegno.callblocker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.fegno.callblocker.ui.theme.LocalCallBlockerColors

data class DeltaInfo(val text: String, val positive: Boolean)

/** Plain colored delta text (reference `+20 %|AVG` style) — no pill, no arrow. */
@Composable
fun DeltaText(delta: DeltaInfo, modifier: Modifier = Modifier, unit: String? = null) {
    val accents = LocalCallBlockerColors.current
    val color = if (delta.positive) accents.deltaPositive else accents.deltaNegative
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = delta.text,
            style = MaterialTheme.typography.labelLarge,
            color = color,
        )
        if (unit != null) {
            Text(
                text = " | $unit",
                style = MaterialTheme.typography.labelMedium,
                color = color.copy(alpha = 0.55f),
                modifier = Modifier.padding(start = 2.dp),
            )
        }
    }
}

@Composable
fun MiniTrack(
    progress: Float,
    modifier: Modifier = Modifier,
    showThumb: Boolean = true,
    trackColor: Color? = null,
    fillColor: Color? = null,
) {
    val accents = LocalCallBlockerColors.current
    val bg = trackColor ?: accents.track
    val fg = fillColor ?: accents.trackFilled
    val clamped = progress.coerceIn(0f, 1f)
    Canvas(
        modifier = modifier
            .height(if (showThumb) 14.dp else 6.dp),
    ) {
        val h = 5.dp.toPx()
        val cy = size.height / 2f
        val r = CornerRadius(h / 2f, h / 2f)
        drawRoundRect(
            color = bg,
            topLeft = Offset(0f, cy - h / 2f),
            size = Size(size.width, h),
            cornerRadius = r,
            style = Fill,
        )
        val filledW = size.width * clamped
        if (filledW > 0f) {
            drawRoundRect(
                color = fg,
                topLeft = Offset(0f, cy - h / 2f),
                size = Size(filledW, h),
                cornerRadius = r,
                style = Fill,
            )
        }
        if (showThumb) {
            val thumbR = 6.dp.toPx()
            val cx = (size.width - thumbR * 2).coerceAtLeast(0f) * clamped + thumbR
            drawCircle(color = fg, radius = thumbR, center = Offset(cx, cy))
        }
    }
}
