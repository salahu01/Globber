package com.salah.callblocker.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom hand-built thin-line icon set (24x24 viewport, stroke-based).
 *
 * Stroke color is a placeholder (Black) — Material3 `Icon(tint = …)` applies a
 * `ColorFilter.tint` over the whole painter, so every glyph inherits its call-site tint.
 */
object AppIcons {

    private fun lineIcon(name: String, build: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                pathData = PathData(build),
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }.build()

    /** Adds a full circle subpath via two half-arcs. */
    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx + r, cy)
        arcTo(r, r, 0f, true, false, cx - r, cy)
        arcTo(r, r, 0f, true, false, cx + r, cy)
    }

    val arrowRight: ImageVector by lazy {
        lineIcon("AppArrowRight") {
            moveTo(4f, 12f); lineTo(20f, 12f)
            moveTo(13f, 5f); lineTo(20f, 12f); lineTo(13f, 19f)
        }
    }

    val arrowLeft: ImageVector by lazy {
        lineIcon("AppArrowLeft") {
            moveTo(20f, 12f); lineTo(4f, 12f)
            moveTo(11f, 5f); lineTo(4f, 12f); lineTo(11f, 19f)
        }
    }

    val chevronUp: ImageVector by lazy {
        lineIcon("AppChevronUp") {
            moveTo(5f, 15f); lineTo(12f, 8f); lineTo(19f, 15f)
        }
    }

    val chevronDown: ImageVector by lazy {
        lineIcon("AppChevronDown") {
            moveTo(5f, 9f); lineTo(12f, 16f); lineTo(19f, 9f)
        }
    }

    val plus: ImageVector by lazy {
        lineIcon("AppPlus") {
            moveTo(12f, 4f); lineTo(12f, 20f)
            moveTo(4f, 12f); lineTo(20f, 12f)
        }
    }

    val trash: ImageVector by lazy {
        lineIcon("AppTrash") {
            // lid
            moveTo(4f, 6.5f); lineTo(20f, 6.5f)
            // handle
            moveTo(9f, 6.5f); lineTo(9.5f, 4.5f); lineTo(14.5f, 4.5f); lineTo(15f, 6.5f)
            // body
            moveTo(6f, 6.5f); lineTo(7f, 20f); lineTo(17f, 20f); lineTo(18f, 6.5f)
            // inner streaks
            moveTo(10f, 10f); lineTo(10.4f, 16.5f)
            moveTo(14f, 10f); lineTo(13.6f, 16.5f)
        }
    }

    val shield: ImageVector by lazy {
        lineIcon("AppShield") {
            moveTo(12f, 3f)
            lineTo(19f, 6f)
            lineTo(19f, 11.5f)
            curveTo(19f, 16f, 16f, 19f, 12f, 20.5f)
            curveTo(8f, 19f, 5f, 16f, 5f, 11.5f)
            lineTo(5f, 6f)
            close()
            // check mark
            moveTo(9f, 11.6f); lineTo(11.2f, 13.8f); lineTo(15f, 9.6f)
        }
    }

    val settings: ImageVector by lazy {
        lineIcon("AppSettings") {
            val bodyR = 5.5f
            val toothInner = 5.5f
            val toothOuter = 8.5f
            // gear body ring + center hole
            circle(12f, 12f, bodyR)
            circle(12f, 12f, 2f)
            // 8 radial teeth
            for (i in 0 until 8) {
                val a = Math.toRadians((i * 45).toDouble())
                val cosA = cos(a).toFloat()
                val sinA = sin(a).toFloat()
                moveTo(12f + cosA * toothInner, 12f + sinA * toothInner)
                lineTo(12f + cosA * toothOuter, 12f + sinA * toothOuter)
            }
        }
    }
}
