package com.seenu.dev.android.vibeplayer.presentation.design_system

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.presentation.theme.Accent
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import java.util.Collections.rotate

@Preview
@Composable
private fun ScanningMusicCardPreview() {
    VibePlayerTheme {
        ScanningMusicCard()
    }
}

@Composable
fun ScanningMusicCard(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun ScanningAnimationPreview() {
    VibePlayerTheme {
        ScanningAnimation(
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun ScanningAnimation(modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.accent
    val accent10 = accent.copy(alpha = 0.1F)
    Canvas(modifier = modifier) {
        val thickCircleWidth = (1.5).dp.toPx()
        val circleWidth = 1.dp.toPx()
        val innerCircleRadius = 8.dp.toPx()
        var radius = size.minDimension / 2F
        val count = 4
        val spaceBetweenCircles = (radius - innerCircleRadius) / count
        for (i in 1 .. count) {
            val (style, color) = when (i) {
                2 -> Stroke(thickCircleWidth) to accent
                else -> Stroke(circleWidth) to accent10
            }

            drawCircle(
                color = color,
                radius = radius,
                style = style
            )
            radius -= spaceBetweenCircles
        }

        drawCircle(
            color = accent,
            radius = innerCircleRadius,
            style = Fill
        )

        val center = Offset(
            size.width / 2F,
            size.height / 2F,
        )
        val end  = Offset(
            size.width / 2F - spaceBetweenCircles,
            size.height / 2F
        )
        drawLine(
            color = accent,
            start = center,
            end = end,
            strokeWidth = 2.dp.toPx()
        )

    }
}