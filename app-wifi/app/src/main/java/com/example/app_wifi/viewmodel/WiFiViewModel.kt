package com.example.app_wifi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_wifi.data.repository.WiFiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de la pantalla de WiFi
 */
class WiFiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WiFiRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(WiFiUiState())
    val uiState: StateFlow<WiFiUiState> = _uiState.asStateFlow()

    init {
        checkWifiStatus()
    }

    /**
     * Verifica el estado actual del WiFi
     */
    fun checkWifiStatus() {
        _uiState.update { currentState ->
            currentState.copy(isWifiEnabled = repository.isWifiEnabled)
        }
    }

    /**
     * Actualiza el estado de los permisos de ubicación
     */
    fun updateLocationPermission(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(hasLocationPermission = granted)
        }
        if (granted) {
            scanNetworks()
        }
    }

    /**
     * Inicia un escaneo de redes WiFi
     */
    fun scanNetworks() {
        if (!_uiState.value.hasLocationPermission) return

        _uiState.update { it.copy(isScanning = true, errorMessage = null) }

        viewModelScope.launch {
            repository.scanWiFiNetworks()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isScanning = false,
                            errorMessage = "Error al escanear: ${exception.message}"
                        )
                    }
                }
                .collect { networks ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            networks = networks,
                            connectedNetwork = repository.getConnectedNetwork(),
                            isScanning = false,
                            isLoading = false,
                            lastScanTime = System.currentTimeMillis(),
                            errorMessage = null
                        )
                    }
                }
        }
    }

    /**
     * Carga las redes disponibles sin iniciar un nuevo escaneo
     */
    fun loadCachedNetworks() {
        if (!_uiState.value.hasLocationPermission) return

        _uiState.update { it.copy(isLoading = true) }

        val networks = repository.getAvailableNetworks()
        val connectedNetwork = repository.getConnectedNetwork()

        _uiState.update { currentState ->
            currentState.copy(
                networks = networks,
                connectedNetwork = connectedNetwork,
                isLoading = false
            )
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Refresca la lista de redes (inicia nuevo escaneo)
     */
    fun refresh() {
        checkWifiStatus()
        if (_uiState.value.isWifiEnabled) {
            scanNetworks()
        }
    }
}
