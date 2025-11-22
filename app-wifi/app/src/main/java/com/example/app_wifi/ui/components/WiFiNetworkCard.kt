package com.example.app_wifi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_wifi.data.model.SecurityType
import com.example.app_wifi.data.model.WiFiNetwork
import com.example.app_wifi.ui.theme.AppwifiTheme

/**
 * Tarjeta que muestra la información de una red WiFi
 */
@Composable
fun WiFiNetworkCard(
    network: WiFiNetwork,
    isConnected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isConnected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono WiFi con indicador de señal
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = null,
                tint = when {
                    network.signalLevel >= 75 -> MaterialTheme.colorScheme.primary
                    network.signalLevel >= 50 -> MaterialTheme.colorScheme.secondary
                    network.signalLevel >= 25 -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.error
                },
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la red
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre de la red y estado de conexión
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = network.ssid.ifBlank { "(Red oculta)" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isConnected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Conectado",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Detalles: frecuencia, canal, seguridad
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecurityBadge(securityType = network.securityType)

                    Text(
                        text = network.frequencyBand,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "CH ${network.channel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Intensidad de señal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${network.signalStrength} dBm",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "(${network.signalQuality})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Barra de señal
            SignalStrengthBar(
                signalLevel = network.signalLevel,
                modifier = Modifier.height(26.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WiFiNetworkCardPreview() {
    AppwifiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WiFiNetworkCard(
                network = WiFiNetwork(
                    ssid = "Mi Casa WiFi",
                    bssid = "AA:BB:CC:DD:EE:FF",
                    signalStrength = -45,
                    frequency = 5180,
                    securityType = SecurityType.WPA2,
                    capabilities = "[WPA2-PSK-CCMP][ESS]"
                ),
                isConnected = true
            )
            WiFiNetworkCard(
                network = WiFiNetwork(
                    ssid = "Vecino_Red",
                    bssid = "11:22:33:44:55:66",
                    signalStrength = -72,
                    frequency = 2437,
                    securityType = SecurityType.WPA3,
                    capabilities = "[WPA3-SAE][ESS]"
                ),
                isConnected = false
            )
            WiFiNetworkCard(
                network = WiFiNetwork(
                    ssid = "Red_Abierta",
                    bssid = "00:11:22:33:44:55",
                    signalStrength = -85,
                    frequency = 2412,
                    securityType = SecurityType.OPEN,
                    capabilities = "[ESS]"
                ),
                isConnected = false
            )
        }
    }
}
