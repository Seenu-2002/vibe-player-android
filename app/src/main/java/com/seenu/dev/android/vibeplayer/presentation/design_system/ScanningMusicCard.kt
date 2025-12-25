package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.R.attr.angle
import android.R.attr.strokeWidth
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.presentation.theme.Accent
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.accent
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyMediumRegular
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
private fun ScanningMusicCardPreview() {
    VibePlayerTheme {
        ScanningMusicCard()
    }
}

@Composable
fun ScanningMusicCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ScanningAnimation(animate = true, modifier = Modifier.size(140.dp))
        Text(
            text = stringResource(R.string.scanning_msg),
            style = MaterialTheme.typography.bodyMediumRegular,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ScanningAnimationPreview() {
    VibePlayerTheme {
        ScanningAnimation(
            modifier = Modifier.size(100.dp),
            animate = false
        )
    }
}

@Composable
fun ScanningAnimation(animate: Boolean, startAngle: Float = 270F, modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.accent
    val accent10 = accent.copy(alpha = 0.1F)

    val angle = remember { Animatable(startAngle) }
    LaunchedEffect(animate) {
        if (animate) {
            while (animate) {
                angle.animateTo(
                    targetValue = startAngle + 360F,
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = LinearEasing
                    )
                )
                angle.snapTo(startAngle)
            }
        } else {
            angle.snapTo(startAngle)
        }
    }

    Canvas(modifier = modifier) {
        val thickCircleWidth = (1.5).dp.toPx()
        val circleWidth = 1.dp.toPx()
        val innerCircleRadius = 6.dp.toPx()
        var radius = size.minDimension / 2F
        val count = 4
        val spaceBetweenCircles = (radius - innerCircleRadius) / count
        for (i in 1..count) {
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
        val theta = Math.toRadians(angle.value.toDouble())

        val center = Offset(
            size.width / 2F,
            size.height / 2F,
        )
        val r = (size.minDimension / 2F) - spaceBetweenCircles
        val end = Offset(
            center.x + (r * cos(theta).toFloat()),
            center.y + (r * sin(theta).toFloat()),
        )
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    accent,
                    Color(0xFFF8FDD6),
                    accent
                )
            ),
            start = center,
            end = end,
            strokeWidth = 2.dp.toPx()
        )

        drawCircle(
            color = accent,
            radius = innerCircleRadius,
            style = Fill
        )

        rotate(degrees = angle.value + 270F, pivot = center) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        accent.copy(alpha = .6F),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                    )
                ),
                startAngle = 0F,
                sweepAngle = 90F,
                style = Fill,
                useCenter = true,
                size = Size(r * 2, r * 2),
                topLeft = Offset(
                    center.x - r,
                    center.y - r
                )
            )
        }
    }
}