package com.pandey.isptoolkit.feature.wifi.signal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pandey.isptoolkit.ui.components.MetricCard
import com.pandey.isptoolkit.ui.components.MetricRowItem
import com.pandey.isptoolkit.ui.components.StatusBadgeType
import com.pandey.isptoolkit.ui.theme.*
import java.util.Locale

@Composable
fun SignalMeterScreen(
    viewModel: SignalMeterViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "LIVE WI-FI SIGNAL METER",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Real-time rolling RSSI sampling & min/max/avg bounds",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!state.isSampling) {
                Button(
                    onClick = { viewModel.startSampling() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("START SAMPLING")
                }
            } else {
                Button(
                    onClick = { viewModel.stopSampling() },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusWarningOrange),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("PAUSE")
                }
            }
            OutlinedButton(
                onClick = { viewModel.resetMeter() },
                modifier = Modifier.weight(1f)
            ) {
                Text("RESET")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "CURRENT RSSI",
                value = state.currentRssi?.let { "$it" } ?: "Unavailable",
                unit = if (state.currentRssi != null) "dBm" else "",
                subtitle = state.ssid ?: "Not Connected",
                statusType = if (state.currentRssi != null) StatusBadgeType.PASS else StatusBadgeType.UNAVAILABLE,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "AVERAGE RSSI",
                value = state.avgRssi?.let { String.format(Locale.US, "%.1f", it) } ?: "Unavailable",
                unit = if (state.avgRssi != null) "dBm" else "",
                subtitle = "Channel: ${state.channel ?: "-"}",
                statusType = StatusBadgeType.INFO,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rolling RSSI Canvas Graph
        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                if (state.samples.size >= 2) {
                    val samples = state.samples
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val minVal = -95f
                        val maxVal = -30f

                        val points = samples.mapIndexed { idx, rssi ->
                            val x = (idx.toFloat() / (samples.size - 1)) * width
                            val clampedRssi = rssi.toFloat().coerceIn(minVal, maxVal)
                            val y = height - ((clampedRssi - minVal) / (maxVal - minVal)) * height
                            Offset(x, y)
                        }

                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                        }

                        drawPath(
                            path = path,
                            color = PrimaryBlueVariant,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                } else {
                    Text(
                        text = if (state.isSampling) "Collecting RSSI samples..." else "Press Start to begin sampling RSSI",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                MetricRowItem(label = "Minimum RSSI", value = state.minRssi?.let { "$it dBm" })
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkBorder)
                MetricRowItem(label = "Maximum RSSI", value = state.maxRssi?.let { "$it dBm" })
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkBorder)
                MetricRowItem(label = "Frequency", value = state.frequency?.let { "$it MHz" })
            }
        }
    }
}
