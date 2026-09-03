package com.pandey.isptoolkit.feature.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun WifiScreen(viewModel: WifiViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Wi-Fi Analyzer", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.scan() }, enabled = !state.isScanning) {
                if (state.isScanning) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "Scan")
                }
                Spacer(Modifier.width(6.dp))
                Text(if (state.isScanning) "Scanning..." else "Scan")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (state.accessPoints.isEmpty() && !state.isScanning) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WifiOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = OnSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Tap Scan to discover Wi-Fi networks", color = OnSurfaceVariant)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.accessPoints) { ap ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Wifi,
                                contentDescription = null,
                                tint = when {
                                    ap.signalDbm >= -50 -> SuccessGreen
                                    ap.signalDbm >= -70 -> WarningAmber
                                    else -> ErrorRed
                                },
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ap.ssid, style = MaterialTheme.typography.titleMedium)
                                Text("${ap.bssid} | Ch ${ap.channel}", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                                Text(ap.capabilities.take(40), style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${ap.signalDbm} dBm", style = MaterialTheme.typography.titleMedium)
                                Text("${ap.frequency} MHz", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
