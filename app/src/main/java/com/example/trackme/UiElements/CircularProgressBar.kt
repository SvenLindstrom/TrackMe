package com.example.trackme.UiElements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CircularProgressBar(
    size: Dp = 300.dp,
    strokeWidth: Dp = 10.dp,
    backgroundArcColor: Color = Color.DarkGray,
    backgroundArcColor2: Color = Color.White,
    angle: Float,
    animator: Float = 1f,
) {
    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundArcColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = backgroundArcColor2,
            startAngle = 270f,
            sweepAngle = angle * animator,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
    }

}