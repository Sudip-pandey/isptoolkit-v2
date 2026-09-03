package com.pandey.isptoolkit.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun HomeScreen(
    onOpenComplaint: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ISP Toolkit", style = MaterialTheme.typography.headlineLarge, color = PrimaryBlue)
        Text("BUILT BY PANDEY", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)

        // Network Status Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Wifi, contentDescription = null, tint = if (state.isConnected) SuccessGreen else ErrorRed)
                    Spacer(Modifier.width(8.dp))
                    Text("Network: ${state.networkType}", style = MaterialTheme.typography.titleMedium)
                }
                if (state.healthScore > 0) {
                    Spacer(Modifier.height(8.dp))
                    Text("Health Score: ${state.healthScore}%", color = when {
                        state.healthScore >= 80 -> SuccessGreen
                        state.healthScore >= 60 -> WarningAmber
                        else -> ErrorRed
                    })
                    Text(state.diagnosticSummary, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Diagnostic Results
        if (state.diagnosticResults.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Diagnostic Results", style = MaterialTheme.typography.titleMedium)
                    state.diagnosticResults.forEach { r ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (r.passed) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (r.passed) SuccessGreen else ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("${r.testName}: ${r.value}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Action Buttons
        Button(
            onClick = { viewModel.runDiagnostics() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
            }
            Spacer(Modifier.width(8.dp))
            Text(if (state.isLoading) "Running..." else "Run Diagnostics")
        }

        OutlinedButton(onClick = onOpenComplaint, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.SupportAgent, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Customer Complaint Troubleshooter")
        }
    }
}
