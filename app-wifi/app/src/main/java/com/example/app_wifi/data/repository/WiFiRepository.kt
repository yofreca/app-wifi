package com.example.app_wifi.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import com.example.app_wifi.data.model.SecurityType
import com.example.app_wifi.data.model.WiFiNetwork
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repositorio para acceder a las funcionalidades de WiFi del sistema
 */
class WiFiRepository(private val context: Context) {

    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * Indica si el WiFi está habilitado en el dispositivo
     */
    val isWifiEnabled: Boolean
        get() = wifiManager.isWifiEnabled

    /**
     * Inicia un escaneo de redes WiFi y retorna los resultados como Flow
     */
    fun scanWiFiNetworks(): Flow<List<WiFiNetwork>> = callbackFlow {
        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                } else {
                    true
                }

                if (success) {
                    val networks = getAvailableNetworks()
                    trySend(networks)
                } else {
                    // Usar resultados en caché si el escaneo falla
                    val networks = getAvailableNetworks()
                    trySend(networks)
                }
            }
        }

        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)

        // Iniciar el escaneo
        @Suppress("DEPRECATION")
        val scanStarted = wifiManager.startScan()

        if (!scanStarted) {
            // Si no se puede iniciar el escaneo, enviar resultados en caché
            val networks = getAvailableNetworks()
            trySend(networks)
        }

        awaitClose {
            context.unregisterReceiver(wifiScanReceiver)
        }
    }

    /**
     * Obtiene la lista de redes WiFi disponibles (resultados del último escaneo)
     */
    @Suppress("DEPRECATION")
    fun getAvailableNetworks(): List<WiFiNetwork> {
        return try {
            wifiManager.scanResults
                .filter { it.SSID.isNotBlank() }
                .distinctBy { it.SSID }
                .map { scanResult ->
                    WiFiNetwork(
                        ssid = scanResult.SSID,
                        bssid = scanResult.BSSID,
                        signalStrength = scanResult.level,
                        frequency = scanResult.frequency,
                        securityType = WiFiNetwork.parseSecurityType(scanResult.capabilities),
                        capabilities = scanResult.capabilities
                    )
                }
                .sortedByDescending { it.signalStrength }
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    /**
     * Información de la red WiFi actualmente conectada
     */
    @Suppress("DEPRECATION")
    fun getConnectedNetwork(): WiFiNetwork? {
        return try {
            val connectionInfo = wifiManager.connectionInfo
            if (connectionInfo != null && connectionInfo.networkId != -1) {
                val ssid = connectionInfo.ssid?.removeSurrounding("\"") ?: return null
                if (ssid == "<unknown ssid>") return null

                WiFiNetwork(
                    ssid = ssid,
                    bssid = connectionInfo.bssid ?: "",
                    signalStrength = connectionInfo.rssi,
                    frequency = connectionInfo.frequency,
                    securityType = SecurityType.UNKNOWN,
                    capabilities = ""
                )
            } else {
                null
            }
        } catch (e: SecurityException) {
            null
        }
    }
}
