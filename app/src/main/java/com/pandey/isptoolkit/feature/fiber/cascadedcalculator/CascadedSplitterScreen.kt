package com.pandey.isptoolkit.feature.fiber.cascadedcalculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun CascadedSplitterScreen(viewModel: CascadedSplitterViewModel = hiltViewModel()) {
    val results by viewModel.results.collectAsState()
    val input by viewModel.inputDbm.collectAsState()
    val stages by viewModel.stages.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Cascaded Splitter Calculator", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = input, onValueChange = { viewModel.inputDbm.value = it }, label = { Text("OLT TX Power (dBm)") }, modifier = Modifier.fillMaxWidth())

        Text("Chain Stages (${stages.size})", style = MaterialTheme.typography.titleMedium)
        stages.forEachIndexed { i, stage ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${i + 1}.", style = MaterialTheme.typography.labelLarge, color = PrimaryBlue, modifier = Modifier.width(28.dp))
                    Text(
                        when (stage) {
                            is com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator.ChainStage.Plc -> "PLC 1:${stage.splitRatio}"
                            is com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator.ChainStage.Coupler -> "Coupler ${stage.portAPercent.toInt()}/${(100 - stage.portAPercent).toInt()}"
                            is com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator.ChainStage.FiberSpan -> "Fiber ${stage.lengthKm}km @${stage.wavelengthNm}nm"
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { viewModel.addFiberStage() }, modifier = Modifier.weight(1f)) { Text("+Fiber") }
            OutlinedButton(onClick = { viewModel.addPlcStage() }, modifier = Modifier.weight(1f)) { Text("+PLC") }
            OutlinedButton(onClick = { viewModel.removeLastStage() }, modifier = Modifier.weight(1f)) { Text("Remove") }
        }

        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate Chain")
        }

        if (results.isNotEmpty()) {
            Text("Stage Results", style = MaterialTheme.typography.titleMedium)
            results.forEach { r ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Stage ${r.stageIndex}: ${r.description}", style = MaterialTheme.typography.labelLarge)
                        Text("Loss: %.2f dB → Output: %.2f dBm".format(r.lossDb, r.outputDbm), color = AccentCyan)
                        Text("Status: ${r.status}", color = if (r.status.contains("Low") || r.status.contains("LOS")) ErrorRed else SuccessGreen, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
