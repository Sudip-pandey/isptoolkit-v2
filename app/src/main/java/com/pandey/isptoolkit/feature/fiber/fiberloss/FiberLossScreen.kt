package com.pandey.isptoolkit.feature.fiber.fiberloss

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.core.calculator.FiberLossCalculator
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun FiberLossScreen(viewModel: FiberLossViewModel = hiltViewModel()) {
    val result by viewModel.result.collectAsState()
    val input by viewModel.inputDbm.collectAsState()
    val length by viewModel.lengthKm.collectAsState()
    val wl by viewModel.wavelength.collectAsState()
    val connectors by viewModel.connectors.collectAsState()
    val splices by viewModel.splices.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Fiber Loss Calculator", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = input, onValueChange = { viewModel.inputDbm.value = it }, label = { Text("TX Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = length, onValueChange = { viewModel.lengthKm.value = it }, label = { Text("Fiber Length (km)") }, modifier = Modifier.fillMaxWidth())

        Text("Wavelength")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FiberLossCalculator.presets.forEach { preset ->
                FilterChip(selected = wl == preset.nm, onClick = { viewModel.wavelength.value = preset.nm }, label = { Text("${preset.nm}nm") })
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Connectors: $connectors", style = MaterialTheme.typography.labelLarge)
                Slider(value = connectors.toFloat(), onValueChange = { viewModel.connectors.value = it.toInt() }, valueRange = 0f..10f, steps = 9)
            }
            Column(Modifier.weight(1f)) {
                Text("Splices: $splices", style = MaterialTheme.typography.labelLarge)
                Slider(value = splices.toFloat(), onValueChange = { viewModel.splices.value = it.toInt() }, valueRange = 0f..20f, steps = 19)
            }
        }

        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate")
        }

        result?.let { r ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Total Loss: %.2f dB".format(r.totalLossDb), style = MaterialTheme.typography.titleMedium, color = ErrorRed)
                    Text("Output: %.2f dBm".format(r.outputDbm), color = AccentCyan)
                    Text("Fiber Loss: %.2f dB (${r.wavelengthNm}nm @ ${r.fiberLengthKm}km)".format(r.fiberLossDb))
                    Text("Connector Loss: %.2f dB (${r.connectorCount} × 0.5dB)".format(r.connectorLossDb))
                    Text("Splice Loss: %.2f dB (${r.spliceCount} × 0.1dB)".format(r.spliceLossDb))
                    Text("Status: ${r.status}", color = if (r.status.contains("Low") || r.status.contains("LOS")) ErrorRed else SuccessGreen)
                }
            }
        }
    }
}
