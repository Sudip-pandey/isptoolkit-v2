package com.pandey.isptoolkit.feature.fiber.ont

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun OntReadingScreen(viewModel: OntReadingViewModel = hiltViewModel()) {
    val reading by viewModel.reading.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("ONT / ONU Readings", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = reading.rxPowerDbm, onValueChange = { viewModel.updateRxPower(it) }, label = { Text("RX Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = reading.txPowerDbm, onValueChange = { viewModel.updateTxPower(it) }, label = { Text("TX Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = reading.voltage, onValueChange = { viewModel.updateVoltage(it) }, label = { Text("Supply Voltage (V)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = reading.temperature, onValueChange = { viewModel.updateTemp(it) }, label = { Text("Temperature (°C)") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = { viewModel.evaluate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Check, null)
            Spacer(Modifier.width(8.dp))
            Text("Evaluate ONT Status")
        }

        if (reading.status != "Unknown") {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ONT Status", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(reading.status, style = MaterialTheme.typography.headlineSmall, color = when {
                        reading.status.contains("LOS") || reading.status.contains("Low") || reading.status.contains("Overload") -> ErrorRed
                        reading.status.contains("Acceptable") || reading.status.contains("Weak") -> WarningAmber
                        else -> SuccessGreen
                    })
                }
            }
        }
    }
}
