package com.example.app_wifi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_wifi.ui.theme.AppwifiTheme

/**
 * Componente visual que muestra la intensidad de la señal WiFi
 * como barras de diferentes alturas
 */
@Composable
fun SignalStrengthBar(
    signalLevel: Int, // 0-100
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val bars = 4
    val activeBars = when {
        signalLevel >= 75 -> 4
        signalLevel >= 50 -> 3
        signalLevel >= 25 -> 2
        signalLevel > 0 -> 1
        else -> 0
    }

    // Color basado en la intensidad
    val signalColor = when {
        signalLevel >= 75 -> Color(0xFF4CAF50) // Verde
        signalLevel >= 50 -> Color(0xFF8BC34A) // Verde claro
        signalLevel >= 25 -> Color(0xFFFFC107) // Amarillo
        else -> Color(0xFFF44336) // Rojo
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in 0 until bars) {
            val barHeight = (8 + (i * 6)).dp
            val isActive = i < activeBars

            SignalBar(
                height = barHeight.value,
                color = if (isActive) signalColor else inactiveColor,
                modifier = Modifier
                    .width(6.dp)
                    .height(barHeight)
            )
        }
    }
}

@Composable
private fun SignalBar(
    height: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val cornerRadius = 2.dp

    androidx.compose.foundation.layout.Box(
        modifier = modifier.drawBehind {
            drawRoundRect(
                color = color,
                topLeft = Offset.Zero,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SignalStrengthBarPreview() {
    AppwifiTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SignalStrengthBar(signalLevel = 100)
            SignalStrengthBar(signalLevel = 60)
            SignalStrengthBar(signalLevel = 30)
            SignalStrengthBar(signalLevel = 10)
        }
    }
}
