package com.pandey.isptoolkit.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pandey.isptoolkit.ui.components.MetricCard
import com.pandey.isptoolkit.ui.components.MetricRowItem
import com.pandey.isptoolkit.ui.components.StatusBadgeType
import com.pandey.isptoolkit.ui.theme.DarkBorder
import com.pandey.isptoolkit.ui.theme.DarkSurface
import com.pandey.isptoolkit.ui.theme.StatusPassGreen
import com.pandey.isptoolkit.ui.theme.TextMuted
import com.pandey.isptoolkit.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier
) {
    val calculations by viewModel.calculationsFlow.collectAsState()
    val sites by viewModel.sitesFlow.collectAsState()

    val demoDiff = BeforeAfterComparison()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "FIELD HISTORY & BEFORE/AFTER COMPARISON",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Saved Optical Calculations, Site Profiles & Visit Diff Metrics",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Before/After Comparison Card
        Text(text = "BEFORE / AFTER COMPARISON", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = demoDiff.siteName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                MetricRowItem(
                    label = "Wi-Fi RSSI Signal",
                    value = "Before: ${demoDiff.beforeRssiDbm} dBm → After: ${demoDiff.afterRssiDbm} dBm (+${demoDiff.afterRssiDbm - demoDiff.beforeRssiDbm} dB)"
                )
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)

                MetricRowItem(
                    label = "Gateway Latency",
                    value = "Before: ${demoDiff.beforeLatencyMs} ms → After: ${demoDiff.afterLatencyMs} ms (-${demoDiff.beforeLatencyMs - demoDiff.afterLatencyMs} ms)"
                )
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)

                MetricRowItem(
                    label = "ONT RX Power",
                    value = "Before: ${demoDiff.beforeOntRxDbm} dBm → After: ${demoDiff.afterOntRxDbm} dBm (+${String.format(Locale.US, "%.1f", demoDiff.afterOntRxDbm - demoDiff.beforeOntRxDbm)} dB)"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Saved Fiber Calculations
        Text(text = "SAVED FIBER CALCULATIONS (${calculations.size})", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(8.dp))

        if (calculations.isEmpty()) {
            Text(
                text = "No saved calculations yet. Use Fiber Calculators and click Save to store results here.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            calculations.forEach { calc ->
                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(calc.timestamp))
                Surface(
                    color = DarkSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = calc.summaryTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = calc.jsonDetails,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(text = "$dateStr | ${calc.notes}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }

                        IconButton(onClick = { viewModel.deleteCalculation(calc.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
