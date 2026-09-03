package com.pandey.isptoolkit.feature.fiber.plccalculator

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.core.calculator.PlcSplitterCalculator
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun PlcSplitterScreen(viewModel: PlcSplitterViewModel = hiltViewModel()) {
    val result by viewModel.result.collectAsState()
    val input by viewModel.inputDbm.collectAsState()
    val ratio by viewModel.splitRatio.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("PLC Splitter Calculator", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = input, onValueChange = { viewModel.inputDbm.value = it }, label = { Text("OLT Output (dBm)") }, modifier = Modifier.fillMaxWidth())

        Text("Split Ratio: 1:$ratio")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PlcSplitterCalculator.availableRatios().forEach { r ->
                FilterChip(selected = ratio == r, onClick = { viewModel.splitRatio.value = r }, label = { Text("1:$r") })
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
                    Text("Output per port: %.2f dBm".format(r.outputDbm), style = MaterialTheme.typography.titleMedium, color = AccentCyan)
                    Text("Theoretical loss: %.1f dB".format(r.theoreticalLossDb))
                    Text("Practical loss: %.1f dB".format(r.practicalLossDb))
                    Text("Status: ${r.status}", color = if (r.status.contains("Low") || r.status.contains("LOS")) ErrorRed else SuccessGreen)
                }
            }
        }
    }
}
