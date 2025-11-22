package com.example.app_wifi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_wifi.ui.screens.PermissionScreen
import com.example.app_wifi.ui.screens.WiFiListScreen
import com.example.app_wifi.ui.theme.AppwifiTheme
import com.example.app_wifi.viewmodel.WiFiViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppwifiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WiFiScannerApp()
                }
            }
        }
    }

    /**
     * Verifica si los permisos de ubicación están concedidos
     */
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun WiFiScannerApp(
    viewModel: WiFiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        viewModel.updateLocationPermission(locationGranted)
    }

    // Verificar permisos al inicio
    LaunchedEffect(Unit) {
        val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }

        // Verificar si ya tenemos permisos
        val hasPermission = permissions.any { permission ->
            ContextCompat.checkSelfPermission(
                viewModel.getApplication(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (hasPermission) {
            viewModel.updateLocationPermission(true)
        }
    }

    // Mostrar pantalla según el estado
    if (uiState.shouldShowPermissionScreen) {
        PermissionScreen(
            onRequestPermission = {
                val permissions = buildList {
                    add(Manifest.permission.ACCESS_FINE_LOCATION)
                    add(Manifest.permission.ACCESS_COARSE_LOCATION)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        add(Manifest.permission.NEARBY_WIFI_DEVICES)
                    }
                }
                permissionLauncher.launch(permissions.toTypedArray())
            }
        )
    } else {
        WiFiListScreen(viewModel = viewModel)
    }
}
