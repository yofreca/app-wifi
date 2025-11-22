package com.example.app_wifi.viewmodel

import com.example.app_wifi.data.model.WiFiNetwork

/**
 * Estado de la UI para la pantalla de WiFi
 */
data class WiFiUiState(
    val networks: List<WiFiNetwork> = emptyList(),
    val connectedNetwork: WiFiNetwork? = null,
    val isLoading: Boolean = false,
    val isScanning: Boolean = false,
    val isWifiEnabled: Boolean = true,
    val hasLocationPermission: Boolean = false,
    val errorMessage: String? = null,
    val lastScanTime: Long = 0
) {
    /**
     * Indica si hay redes disponibles
     */
    val hasNetworks: Boolean
        get() = networks.isNotEmpty()

    /**
     * Cantidad de redes detectadas
     */
    val networkCount: Int
        get() = networks.size

    /**
     * Indica si se debe mostrar la pantalla de permisos
     */
    val shouldShowPermissionScreen: Boolean
        get() = !hasLocationPermission

    /**
     * Indica si se debe mostrar mensaje de WiFi deshabilitado
     */
    val shouldShowWifiDisabled: Boolean
        get() = !isWifiEnabled && hasLocationPermission
}
