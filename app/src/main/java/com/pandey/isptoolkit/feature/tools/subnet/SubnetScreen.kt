package com.pandey.isptoolkit.feature.tools.subnet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun SubnetScreen(viewModel: SubnetViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Subnet Calculator", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.ipAddress,
            onValueChange = { viewModel.updateIp(it) },
            label = { Text("IP Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("CIDR: /${state.cidr}", modifier = Modifier.width(80.dp))
            Slider(
                value = state.cidr.toFloat(),
                onValueChange = { viewModel.updateCidr(it.toInt()) },
                valueRange = 0f..32f,
                steps = 31,
                modifier = Modifier.weight(1f)
            )
        }

        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate")
        }

        state.error?.let { Text(it, color = ErrorRed) }

        state.result?.let { r ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Results", style = MaterialTheme.typography.titleLarge)
                    ResultRow("IP Address", "${r.ipAddress}/${r.cidr}")
                    ResultRow("Subnet Mask", r.subnetMask)
                    ResultRow("Network", r.networkAddress)
                    ResultRow("Broadcast", r.broadcastAddress)
                    ResultRow("First Host", r.firstHost)
                    ResultRow("Last Host", r.lastHost)
                    ResultRow("Total Hosts", r.totalHosts.toString())
                    ResultRow("Usable Hosts", r.usableHosts.toString())
                }
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), color = OnSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
