# WiFi Scanner App

Aplicación Android para escanear y visualizar redes WiFi cercanas, mostrando la intensidad de señal e información detallada de cada red.

## Descripcion

Esta aplicación permite identificar todas las redes WiFi disponibles en el entorno, presentando información relevante como:

- Nombre de la red (SSID)
- Intensidad de la señal (en dBm y porcentaje visual)
- Tipo de seguridad (WPA, WPA2, WPA3, Open)
- Frecuencia (2.4 GHz / 5 GHz)
- Canal de operación
- Dirección MAC (BSSID)

## Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| **Lenguaje** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Gestión de Estado** | ViewModel + StateFlow |
| **Arquitectura** | MVVM (Model-View-ViewModel) |
| **Min SDK** | API 24 (Android 7.0) |
| **Target SDK** | API 36 (Android 15) |

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│  ┌─────────────────────────────────────────────────┐   │
│  │            Jetpack Compose Screens              │   │
│  │  - WiFiListScreen                               │   │
│  │  - WiFiDetailScreen                             │   │
│  │  - SignalStrengthIndicator                      │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                   ViewModel Layer                       │
│  ┌─────────────────────────────────────────────────┐   │
│  │              WiFiViewModel                      │   │
│  │  - UI State (StateFlow)                         │   │
│  │  - Scan Logic                                   │   │
│  │  - Filter/Sort                                  │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    Data Layer                           │
│  ┌─────────────────────────────────────────────────┐   │
│  │            WiFiRepository                       │   │
│  │  - WifiManager Integration                      │   │
│  │  - BroadcastReceiver                            │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## Estructura del Proyecto

```
app/src/main/java/com/example/app_wifi/
├── MainActivity.kt
├── data/
│   ├── model/
│   │   └── WiFiNetwork.kt          # Modelo de datos de red WiFi
│   └── repository/
│       └── WiFiRepository.kt       # Acceso a datos WiFi del sistema
├── ui/
│   ├── screens/
│   │   ├── WiFiListScreen.kt       # Pantalla principal con lista
│   │   └── WiFiDetailScreen.kt     # Detalle de red seleccionada
│   ├── components/
│   │   ├── WiFiNetworkCard.kt      # Tarjeta de red individual
│   │   ├── SignalStrengthBar.kt    # Indicador visual de señal
│   │   └── SecurityBadge.kt        # Badge de tipo de seguridad
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── viewmodel/
    └── WiFiViewModel.kt            # ViewModel con estado de la app
```

## Permisos Requeridos

```xml
<!-- Permisos para escanear redes WiFi -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Para Android 13+ -->
<uses-permission android:name="android.permission.NEARBY_WIFI_DEVICES" />
```

## Funcionalidades

### Implementadas
- [ ] Escaneo de redes WiFi cercanas
- [ ] Visualización de lista de redes
- [ ] Indicador de intensidad de señal
- [ ] Información detallada de cada red
- [ ] Filtrado por tipo de seguridad
- [ ] Ordenamiento por intensidad de señal
- [ ] Actualización automática periódica
- [ ] Soporte para modo oscuro

### Futuras
- [ ] Historial de redes detectadas
- [ ] Gráfico de intensidad de señal en tiempo real
- [ ] Exportar lista de redes a archivo
- [ ] Notificaciones de redes conocidas

## Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Dispositivo Android físico (el emulador tiene limitaciones para WiFi)

## Instalación

1. Clonar el repositorio:
```bash
git clone <repository-url>
cd app-wifi
```

2. Abrir en Android Studio

3. Sincronizar Gradle

4. Ejecutar en dispositivo físico

## Uso

1. Al iniciar la app, se solicitarán permisos de ubicación (necesarios para escanear WiFi)
2. La pantalla principal mostrará la lista de redes detectadas
3. Pulsar en una red para ver información detallada
4. Usar el botón de actualizar para realizar un nuevo escaneo

## Interpretación de la Señal

| Intensidad (dBm) | Calidad | Indicador |
|------------------|---------|-----------|
| -30 a -50 | Excelente | ████████ |
| -50 a -60 | Buena | ██████░░ |
| -60 a -70 | Aceptable | ████░░░░ |
| -70 a -80 | Débil | ██░░░░░░ |
| < -80 | Muy débil | █░░░░░░░ |

## Licencia

Este proyecto es de uso educativo y demostrativo.
