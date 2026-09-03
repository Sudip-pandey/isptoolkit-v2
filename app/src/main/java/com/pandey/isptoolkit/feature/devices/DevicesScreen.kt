package com.pandey.isptoolkit.feature.devices

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
fun DevicesScreen(viewModel: DevicesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("LAN Devices", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.scanLan() }, enabled = !state.isScanning) {
                if (state.isScanning) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Scan")
                }
                Spacer(Modifier.width(6.dp))
                Text(if (state.isScanning) "Scanning..." else "Scan LAN")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (state.devices.isEmpty() && !state.isScanning) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DevicesOther, contentDescription = null, modifier = Modifier.size(64.dp), tint = OnSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Tap Scan LAN to discover devices", color = OnSurfaceVariant)
                }
            }
        } else {
            Text("Found ${state.devices.size} device(s)", color = OnSurfaceVariant, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.devices) { device ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Computer, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(32.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(device.ipAddress, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    if (device.hostname == device.ipAddress) "Hostname: Not resolved" else device.hostname,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariant
                                )
                                Text("${device.latencyMs}ms", style = MaterialTheme.typography.labelMedium, color = SuccessGreen)
                            }
                        }
                    }
                }
            }
        }
    }
}
