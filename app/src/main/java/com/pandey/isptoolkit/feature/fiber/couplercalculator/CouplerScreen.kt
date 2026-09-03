package com.pandey.isptoolkit.feature.fiber.couplercalculator

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun CouplerScreen(viewModel: CouplerViewModel = hiltViewModel()) {
    val result by viewModel.result.collectAsState()
    val input by viewModel.inputDbm.collectAsState()
    val portA by viewModel.portAPercent.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Optical Coupler Calculator", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = input, onValueChange = { viewModel.inputDbm.value = it }, label = { Text("Input Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Port A: ${portA.toInt()}% / Port B: ${(100 - portA).toInt()}%", modifier = Modifier.weight(1f))
        }
        Slider(value = portA.toFloat(), onValueChange = { viewModel.portAPercent.value = it.toDouble() }, valueRange = 5f..95f, modifier = Modifier.fillMaxWidth())
        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate")
        }
        result?.let { r ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Port A (${r.portARatio.toInt()}%)", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                        Text("%.2f dBm".format(r.portAOutputDbm), color = AccentCyan)
                        Text(r.portAStatus, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Port B (${r.portBRatio.toInt()}%)", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                        Text("%.2f dBm".format(r.portBOutputDbm), color = PrimaryBlueLight)
                        Text(r.portBStatus, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
