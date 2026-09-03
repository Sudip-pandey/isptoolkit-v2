package com.pandey.isptoolkit.feature.wifi.signal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun SignalMeterScreen(viewModel: SignalMeterViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Signal Meter", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                    Text("${state.currentDbm}", style = MaterialTheme.typography.titleLarge, color = AccentCyan)
                    Text("dBm", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Min", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                    Text("${state.minDbm}", style = MaterialTheme.typography.titleLarge, color = ErrorRed)
                    Text("dBm", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Max", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                    Text("${state.maxDbm}", style = MaterialTheme.typography.titleLarge, color = SuccessGreen)
                    Text("dBm", style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
                }
            }
        }

        // Signal Graph Canvas
        Card(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                if (state.history.size > 1) {
                    drawSignalGraph(state.history)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { viewModel.startMonitoring() },
                modifier = Modifier.weight(1f),
                enabled = !state.isRunning
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Start")
            }
            OutlinedButton(
                onClick = { viewModel.stopMonitoring() },
                modifier = Modifier.weight(1f),
                enabled = state.isRunning
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Stop")
            }
        }
    }
}

private fun DrawScope.drawSignalGraph(history: List<Int>) {
    val minVal = -100f
    val maxVal = -30f
    val stepX = size.width / (history.size - 1).coerceAtLeast(1)
    val points = history.mapIndexed { i, dbm ->
        val x = i * stepX
        val normalized = ((dbm - minVal) / (maxVal - minVal)).coerceIn(0f, 1f)
        val y = size.height - (normalized * size.height)
        Offset(x, y)
    }
    for (i in 0 until points.size - 1) {
        drawLine(Color(0xFF0EA5E9), points[i], points[i + 1], strokeWidth = 2.dp.toPx())
    }
}
