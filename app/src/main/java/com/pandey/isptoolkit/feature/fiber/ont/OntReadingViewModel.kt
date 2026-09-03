package com.pandey.isptoolkit.feature.fiber.ont

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class OntReading(
    val rxPowerDbm: String = "",
    val txPowerDbm: String = "",
    val voltage: String = "",
    val temperature: String = "",
    val status: String = "Unknown"
)

@HiltViewModel
class OntReadingViewModel @Inject constructor() : ViewModel() {
    private val _reading = MutableStateFlow(OntReading())
    val reading: StateFlow<OntReading> = _reading

    fun updateRxPower(v: String) { _reading.value = _reading.value.copy(rxPowerDbm = v) }
    fun updateTxPower(v: String) { _reading.value = _reading.value.copy(txPowerDbm = v) }
    fun updateVoltage(v: String) { _reading.value = _reading.value.copy(voltage = v) }
    fun updateTemp(v: String) { _reading.value = _reading.value.copy(temperature = v) }

    fun evaluate() {
        val rx = _reading.value.rxPowerDbm.toDoubleOrNull()
        val status = when {
            rx == null -> "Unknown"
            rx < -30 -> "LOS - Signal Too Low"
            rx > 0 -> "Overload Risk"
            rx >= -10 -> "Excellent"
            rx >= -20 -> "Good"
            rx >= -27 -> "Acceptable"
            else -> "Weak Signal"
        }
        _reading.value = _reading.value.copy(status = status)
    }
}
