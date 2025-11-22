package com.example.app_wifi.data.model

/**
 * Tipos de seguridad de red WiFi
 */
enum class SecurityType {
    OPEN,
    WEP,
    WPA,
    WPA2,
    WPA3,
    UNKNOWN
}

/**
 * Modelo de datos que representa una red WiFi detectada
 */
data class WiFiNetwork(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int, // en dBm (típicamente -30 a -100)
    val frequency: Int, // en MHz
    val securityType: SecurityType,
    val capabilities: String // cadena raw de capacidades
) {
    /**
     * Nivel de señal normalizado de 0 a 100
     */
    val signalLevel: Int
        get() = calculateSignalLevel(signalStrength)

    /**
     * Indica si la red opera en banda de 5GHz
     */
    val is5GHz: Boolean
        get() = frequency >= 5000

    /**
     * Indica si la red opera en banda de 2.4GHz
     */
    val is24GHz: Boolean
        get() = frequency in 2400..2500

    /**
     * Número de canal WiFi basado en la frecuencia
     */
    val channel: Int
        get() = frequencyToChannel(frequency)

    /**
     * Banda de frecuencia como texto legible
     */
    val frequencyBand: String
        get() = when {
            is24GHz -> "2.4 GHz"
            is5GHz -> "5 GHz"
            else -> "$frequency MHz"
        }

    /**
     * Calidad de la señal como texto
     */
    val signalQuality: String
        get() = when {
            signalStrength >= -50 -> "Excelente"
            signalStrength >= -60 -> "Buena"
            signalStrength >= -70 -> "Aceptable"
            signalStrength >= -80 -> "Débil"
            else -> "Muy débil"
        }

    companion object {
        /**
         * Convierte dBm a un nivel de 0-100
         */
        fun calculateSignalLevel(dBm: Int): Int {
            return when {
                dBm >= -50 -> 100
                dBm <= -100 -> 0
                else -> 2 * (dBm + 100)
            }
        }

        /**
         * Convierte frecuencia en MHz a número de canal
         */
        fun frequencyToChannel(frequency: Int): Int {
            return when {
                frequency in 2412..2484 -> (frequency - 2412) / 5 + 1
                frequency in 5170..5825 -> (frequency - 5170) / 5 + 34
                else -> 0
            }
        }

        /**
         * Detecta el tipo de seguridad desde las capacidades
         */
        fun parseSecurityType(capabilities: String): SecurityType {
            return when {
                capabilities.contains("WPA3") -> SecurityType.WPA3
                capabilities.contains("WPA2") -> SecurityType.WPA2
                capabilities.contains("WPA") -> SecurityType.WPA
                capabilities.contains("WEP") -> SecurityType.WEP
                capabilities.contains("ESS") && !capabilities.contains("WPA")
                    && !capabilities.contains("WEP") -> SecurityType.OPEN
                else -> SecurityType.UNKNOWN
            }
        }
    }
}
