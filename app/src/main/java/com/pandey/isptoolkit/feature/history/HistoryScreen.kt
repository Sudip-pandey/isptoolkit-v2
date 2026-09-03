package com.pandey.isptoolkit.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pandey.isptoolkit.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val calcs by viewModel.calculations.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("History", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (calcs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(64.dp), tint = OnSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("No saved calculations yet", color = OnSurfaceVariant)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calcs) { calc ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(calc.type, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                Text(
                                    SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(Date(calc.timestamp)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = OnSurfaceVariant
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Column {
                                    Text("Input", color = OnSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                    Text("%.2f dBm".format(calc.inputDbm), color = AccentCyan)
                                }
                                Column {
                                    Text("Output", color = OnSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                    Text("%.2f dBm".format(calc.outputDbm))
                                }
                                Column {
                                    Text("Loss", color = OnSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                                    Text("%.2f dB".format(calc.lossDb), color = ErrorRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
