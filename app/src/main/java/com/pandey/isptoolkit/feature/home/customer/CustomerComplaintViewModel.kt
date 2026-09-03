package com.pandey.isptoolkit.feature.home.customer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class ComplaintType {
    SLOW_INTERNET, NO_INTERNET, WEAK_WIFI, HIGH_PING, ONT_LOS
}

data class TroubleshootStep(
    val step: Int,
    val action: String,
    val result: String? = null
)

@HiltViewModel
class CustomerComplaintViewModel @Inject constructor() : ViewModel() {
    private val _complaint = MutableStateFlow<ComplaintType?>(null)
    val complaint: StateFlow<ComplaintType?> = _complaint

    private val _steps = MutableStateFlow<List<TroubleshootStep>>(emptyList())
    val steps: StateFlow<List<TroubleshootStep>> = _steps

    fun selectComplaint(type: ComplaintType) {
        _complaint.value = type
        _steps.value = getStepsFor(type)
    }

    private fun getStepsFor(type: ComplaintType): List<TroubleshootStep> = when (type) {
        ComplaintType.SLOW_INTERNET -> listOf(
            TroubleshootStep(1, "Check ONT RX power level (expected: -8 to -27 dBm)"),
            TroubleshootStep(2, "Run speed test to verify actual throughput"),
            TroubleshootStep(3, "Check for Wi-Fi interference (2.4GHz vs 5GHz)"),
            TroubleshootStep(4, "Verify no background bandwidth hogs on network"),
            TroubleshootStep(5, "Reboot ONT and router if above checks pass")
        )
        ComplaintType.NO_INTERNET -> listOf(
            TroubleshootStep(1, "Check ONT LED status (PON/LOS indicators)"),
            TroubleshootStep(2, "Verify fiber cable connector is seated properly"),
            TroubleshootStep(3, "Check PPPoE credentials if applicable"),
            TroubleshootStep(4, "Ping default gateway to verify LAN connectivity"),
            TroubleshootStep(5, "Contact NOC if ONT shows LOS/LDOWN state")
        )
        ComplaintType.WEAK_WIFI -> listOf(
            TroubleshootStep(1, "Measure RSSI at customer device location"),
            TroubleshootStep(2, "Check channel utilization and interference"),
            TroubleshootStep(3, "Verify router antenna orientation"),
            TroubleshootStep(4, "Consider AP placement or additional AP"),
            TroubleshootStep(5, "Switch to 5GHz band if device supports it")
        )
        ComplaintType.HIGH_PING -> listOf(
            TroubleshootStep(1, "Run ping to gateway and note latency"),
            TroubleshootStep(2, "Check for upstream traffic congestion"),
            TroubleshootStep(3, "Verify QoS settings on router"),
            TroubleshootStep(4, "Check for DNS resolution delay"),
            TroubleshootStep(5, "Escalate to NOC if latency > 50ms consistently")
        )
        ComplaintType.ONT_LOS -> listOf(
            TroubleshootStep(1, "Check fiber patch cord for physical damage"),
            TroubleshootStep(2, "Clean optical connectors with proper cleaner"),
            TroubleshootStep(3, "Measure RX power at ONT port"),
            TroubleshootStep(4, "Check splitter port and upstream fiber"),
            TroubleshootStep(5, "Replace fiber if loss exceeds acceptable threshold")
        )
    }
}
