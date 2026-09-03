package com.pandey.isptoolkit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pandey.isptoolkit.data.repository.ConnectionType
import com.pandey.isptoolkit.ui.components.MetricCard
import com.pandey.isptoolkit.ui.components.MetricRowItem
import com.pandey.isptoolkit.ui.components.StatusBadgeType
import com.pandey.isptoolkit.ui.components.StatusChip
import com.pandey.isptoolkit.ui.theme.DarkBorder
import com.pandey.isptoolkit.ui.theme.DarkSurface
import com.pandey.isptoolkit.ui.theme.TextMuted
import com.pandey.isptoolkit.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToFiber: () -> Unit,
    onNavigateToWifi: () -> Unit,
    onNavigateToDevices: () -> Unit,
    onNavigateToComplaint: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val netState = state.networkState

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Branding Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ISP TOOLKIT",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "BUILT BY PANDEY | Field Technician Suite",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = { viewModel.refreshNetworkState() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh State")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Connection Banner
        val connBadge = when (netState?.connectionType) {
            ConnectionType.CONNECTED_WIFI -> StatusBadgeType.PASS
            ConnectionType.CONNECTED_CELLULAR -> StatusBadgeType.INFO
            ConnectionType.CONNECTED_ETHERNET -> StatusBadgeType.PASS
            ConnectionType.CONNECTED_VPN -> StatusBadgeType.WARNING
            ConnectionType.NO_INTERNET -> StatusBadgeType.WARNING
            else -> StatusBadgeType.FAIL
        }

        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "STATUS: ${netState?.connectionType?.name ?: "UNKNOWN"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (netState?.isInternetValidated == true) "Validated Internet Access" else "Limited / Unvalidated Connection",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                StatusChip(text = connBadge.name, type = connBadge)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4 Main Metric Cards
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                title = "PING",
                value = state.lastPingMs,
                subtitle = "Gateway / WAN",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "RSSI",
                value = netState?.rssiDbm?.let { "$it" } ?: "Unavailable",
                unit = if (netState?.rssiDbm != null) "dBm" else "",
                subtitle = netState?.ssid ?: "No SSID",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                title = "DOWNLOAD",
                value = state.downloadSpeedMbps,
                subtitle = "Measured Speed",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "UPLOAD",
                value = state.uploadSpeedMbps,
                subtitle = "Measured Speed",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Network Summary Card
        Text(text = "NETWORK SUMMARY", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                MetricRowItem(label = "SSID", value = netState?.ssid)
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)
                MetricRowItem(label = "IPv4 Address", value = netState?.ipv4Address)
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)
                MetricRowItem(label = "Gateway", value = netState?.gateway)
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)
                MetricRowItem(label = "DNS Servers", value = netState?.dnsServers?.joinToString(", ")?.ifEmpty { null })
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = DarkBorder)
                MetricRowItem(label = "Channel / Frequency", value = netState?.channel?.let { "Ch $it (${netState.frequencyMhz} MHz)" })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Tools Grid
        Text(text = "QUICK TOOLS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { viewModel.runQuickDiagnostics() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("FULL DIAGNOSTIC")
            }
            Button(
                onClick = onNavigateToFiber,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("FIBER TOOLS")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onNavigateToWifi, modifier = Modifier.weight(1f)) {
                Text("WI-FI SIGNAL")
            }
            OutlinedButton(onClick = onNavigateToDevices, modifier = Modifier.weight(1f)) {
                Text("DEVICE SCAN")
            }
            OutlinedButton(onClick = { viewModel.runSpeedTest() }, modifier = Modifier.weight(1f)) {
                Text("SPEED TEST")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Complaints Shortcut
        Text(text = "CUSTOMER COMPLAINT MODE", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedButton(
            onClick = onNavigateToComplaint,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LAUNCH COMPLAINT TROUBLESHOOTER")
        }
    }
}
