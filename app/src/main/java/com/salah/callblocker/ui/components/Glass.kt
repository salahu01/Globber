package com.salah.callblocker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** Flat near-black backdrop (reference design has no ambient glow). */
@Composable
fun GlassBackdrop(modifier: Modifier = Modifier) {
    val bg = MaterialTheme.colorScheme.background
    Canvas(modifier) {
        drawRect(bg)
    }
}

/** Solid neutral card surface with a hairline edge — matches the reference's flat dark cards. */
@Composable
fun Modifier.glassSurface(shape: Shape): Modifier {
    val scheme = MaterialTheme.colorScheme
    val edge = scheme.onSurface.copy(alpha = 0.08f)
    return this
        .clip(shape)
        .background(scheme.surfaceVariant)
        .border(1.dp, edge, shape)
}
