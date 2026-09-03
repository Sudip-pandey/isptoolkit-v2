package com.pandey.isptoolkit.feature.fiber.opticalpower

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun OpticalPowerScreen(viewModel: OpticalPowerViewModel = hiltViewModel()) {
    val result by viewModel.result.collectAsState()
    val input by viewModel.inputDbm.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Optical Power Converter", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = input, onValueChange = { viewModel.updateInput(it) }, label = { Text("Input Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Convert")
        }
        result?.let { r ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("%.2f dBm".format(r.dbm), style = MaterialTheme.typography.headlineMedium, color = AccentCyan)
                    Text("%.4f mW".format(r.milliwatts))
                    Text("%.2f µW".format(r.microwatts))
                    Text("Status: ${r.status}", color = when {
                        r.status.startsWith("Excellent") -> SuccessGreen
                        r.status.startsWith("Good") -> SuccessGreen
                        r.status.startsWith("Fair") -> WarningAmber
                        else -> ErrorRed
                    })
                }
            }
        }
    }
}
