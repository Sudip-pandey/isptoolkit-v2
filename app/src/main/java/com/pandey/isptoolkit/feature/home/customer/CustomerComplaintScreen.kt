package com.pandey.isptoolkit.feature.home.customer

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
fun CustomerComplaintScreen(
    viewModel: CustomerComplaintViewModel = hiltViewModel()
) {
    val complaint by viewModel.complaint.collectAsState()
    val steps by viewModel.steps.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Customer Complaint", style = MaterialTheme.typography.headlineMedium)
        Text("Select issue type to get guided troubleshooting steps", color = OnSurfaceVariant)

        val complaints = listOf(
            ComplaintType.SLOW_INTERNET to "Slow Internet",
            ComplaintType.NO_INTERNET to "No Internet",
            ComplaintType.WEAK_WIFI to "Weak Wi-Fi",
            ComplaintType.HIGH_PING to "High Ping / Lag",
            ComplaintType.ONT_LOS to "ONT Red LOS"
        )

        complaints.forEach { (type, label) ->
            OutlinedButton(
                onClick = { viewModel.selectComplaint(type) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (complaint == type) PrimaryBlue.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Text(label)
            }
        }

        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text("Troubleshooting Steps", style = MaterialTheme.typography.titleLarge)
            steps.forEach { step ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("${step.step}", style = MaterialTheme.typography.labelMedium, color = OnSurface)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(step.action, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
