package com.pandey.isptoolkit.feature.fiber.linkbudget

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
fun LinkBudgetScreen(viewModel: LinkBudgetViewModel = hiltViewModel()) {
    val result by viewModel.result.collectAsState()
    val tx by viewModel.txPower.collectAsState()
    val loss by viewModel.totalLoss.collectAsState()
    val rx by viewModel.rxSensitivity.collectAsState()
    val ol by viewModel.overload.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Link Budget Calculator", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = tx, onValueChange = { viewModel.txPower.value = it }, label = { Text("TX Power (dBm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = loss, onValueChange = { viewModel.totalLoss.value = it }, label = { Text("Total Loss (dB)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = rx, onValueChange = { viewModel.rxSensitivity.value = it }, label = { Text("RX Sensitivity (dBm, e.g. -28)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ol, onValueChange = { viewModel.overload.value = it }, label = { Text("RX Overload (dBm, e.g. 0)") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate")
        }

        result?.let { r ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("RX Power: %.2f dBm".format(r.rxPowerDbm), style = MaterialTheme.typography.titleMedium, color = AccentCyan)
                    Text("Link Margin: %.2f dB".format(r.linkMarginDb), color = if (r.linkMarginDb >= 3) SuccessGreen else ErrorRed)
                    Text("Status: ${r.statusLabel}", color = when (r.status) {
                        com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator.LinkStatus.EXCELLENT -> SuccessGreen
                        com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator.LinkStatus.GOOD -> SuccessGreen
                        com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator.LinkStatus.MARGINAL -> WarningAmber
                        com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator.LinkStatus.INSUFFICIENT -> ErrorRed
                    })
                }
            }
        }
    }
}
