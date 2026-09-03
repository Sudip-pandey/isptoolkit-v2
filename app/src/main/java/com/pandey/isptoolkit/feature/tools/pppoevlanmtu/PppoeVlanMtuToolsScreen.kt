package com.pandey.isptoolkit.feature.tools.pppoevlanmtu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pandey.isptoolkit.ui.theme.*

@Composable
fun PppoeVlanMtuToolsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PPPoE / VLAN / MTU Tools", style = MaterialTheme.typography.headlineMedium)

        // MTU Calculator
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("MTU Overhead Reference", style = MaterialTheme.typography.titleLarge)
                MtuRow("Standard Ethernet MTU", "1500 bytes")
                MtuRow("PPPoE Overhead", "8 bytes")
                MtuRow("PPPoE Effective MTU", "1492 bytes")
                MtuRow("VLAN Tag (802.1Q)", "4 bytes overhead")
                MtuRow("PPPoE + VLAN", "1488 bytes effective")
                MtuRow("IPv6 min MTU", "1280 bytes")
            }
        }

        // VLAN Reference
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("VLAN Reference", style = MaterialTheme.typography.titleLarge)
                MtuRow("VLAN ID Range", "1 – 4094")
                MtuRow("Default VLAN", "1")
                MtuRow("Management VLAN", "Typically 10 or 99")
                MtuRow("ISP Customer VLAN", "100 – 3999 (common)")
                MtuRow("Native VLAN (trunk)", "Untagged on trunk links")
                MtuRow("802.1p PCP Range", "0 (BE) to 7 (NC)")
            }
        }

        // PCP Priority Reference
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("802.1p Priority Reference", style = MaterialTheme.typography.titleLarge)
                val pcpValues = listOf(
                    "7" to "Network Control (NC)",
                    "6" to "Internetwork Control",
                    "5" to "Voice < 10ms",
                    "4" to "Video < 100ms",
                    "3" to "Critical Apps",
                    "2" to "Excellent Effort",
                    "1" to "Background",
                    "0" to "Best Effort (BE)"
                )
                pcpValues.forEach { (pcp, desc) ->
                    MtuRow("PCP $pcp", desc)
                }
            }
        }
    }
}

@Composable
private fun MtuRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), color = OnSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
