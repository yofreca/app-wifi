package com.example.app_wifi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_wifi.data.model.SecurityType
import com.example.app_wifi.ui.theme.AppwifiTheme

/**
 * Badge que muestra el tipo de seguridad de una red WiFi
 */
@Composable
fun SecurityBadge(
    securityType: SecurityType,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, icon, label) = getSecurityStyle(securityType)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

/**
 * Obtiene el estilo visual para cada tipo de seguridad
 */
private fun getSecurityStyle(securityType: SecurityType): SecurityStyle {
    return when (securityType) {
        SecurityType.OPEN -> SecurityStyle(
            backgroundColor = Color(0xFFFFEBEE),
            textColor = Color(0xFFC62828),
            icon = Icons.Default.LockOpen,
            label = "Abierta"
        )
        SecurityType.WEP -> SecurityStyle(
            backgroundColor = Color(0xFFFFF3E0),
            textColor = Color(0xFFEF6C00),
            icon = Icons.Default.Lock,
            label = "WEP"
        )
        SecurityType.WPA -> SecurityStyle(
            backgroundColor = Color(0xFFFFF8E1),
            textColor = Color(0xFFF9A825),
            icon = Icons.Default.Lock,
            label = "WPA"
        )
        SecurityType.WPA2 -> SecurityStyle(
            backgroundColor = Color(0xFFE8F5E9),
            textColor = Color(0xFF2E7D32),
            icon = Icons.Default.Shield,
            label = "WPA2"
        )
        SecurityType.WPA3 -> SecurityStyle(
            backgroundColor = Color(0xFFE3F2FD),
            textColor = Color(0xFF1565C0),
            icon = Icons.Default.Shield,
            label = "WPA3"
        )
        SecurityType.UNKNOWN -> SecurityStyle(
            backgroundColor = Color(0xFFF5F5F5),
            textColor = Color(0xFF757575),
            icon = Icons.Default.Lock,
            label = "Desconocido"
        )
    }
}

private data class SecurityStyle(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val label: String
)

@Preview(showBackground = true)
@Composable
private fun SecurityBadgePreview() {
    AppwifiTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            SecurityBadge(securityType = SecurityType.OPEN)
            SecurityBadge(securityType = SecurityType.WPA2)
            SecurityBadge(securityType = SecurityType.WPA3)
        }
    }
}
