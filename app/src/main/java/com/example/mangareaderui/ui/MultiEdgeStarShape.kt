package com.example.mangareaderui.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.lang.Math.cos
import java.lang.Math.min
import java.lang.Math.sin


class MultiEdgeStarShape: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawMultiEdgeStarShape()
        )
    }

    private fun drawMultiEdgeStarShape(): Path {
        return Path().apply {
            reset()

            val size = 200.dp

            val centerX = size.value / 2
            val centerY = size.value / 2

            val outerRadius = size.value / 2 * 0.3f
            val innerRadius = outerRadius / 2.5f

            val degreesPerPoint = 360f / 7f

            val initialAngleOffset = -90f - degreesPerPoint / 2f // Adjust the initial angle offset

            for (i in 0 until 7) {
                val outerAngle = degreesPerPoint * i + initialAngleOffset
                val innerAngle = outerAngle + degreesPerPoint / 2f

                val outerX = centerX + outerRadius * cos(Math.toRadians(outerAngle.toDouble())).toFloat()
                val outerY = centerY + outerRadius * sin(Math.toRadians(outerAngle.toDouble())).toFloat()

                val innerX = centerX + innerRadius * cos(Math.toRadians(innerAngle.toDouble())).toFloat()
                val innerY = centerY + innerRadius * sin(Math.toRadians(innerAngle.toDouble())).toFloat()

                if (i == 0) {
                    moveTo(outerX, outerY) // Move to the first outer point
                    lineTo(innerX, innerY) // Draw a line to the first inner point
                } else {
                    lineTo(outerX, outerY)
                    lineTo(innerX, innerY)
                }
            }






            close() // Close the path to connect the last point to the first point
        }
    }
}