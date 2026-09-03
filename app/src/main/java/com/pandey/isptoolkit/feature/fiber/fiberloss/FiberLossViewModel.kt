package com.pandey.isptoolkit.feature.fiber.fiberloss

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.FiberLossCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FiberLossViewModel @Inject constructor() : ViewModel() {
    val inputDbm = MutableStateFlow("3.0")
    val wavelength = MutableStateFlow(1490)
    val lengthKm = MutableStateFlow("1.0")
    val connectors = MutableStateFlow(2)
    val splices = MutableStateFlow(1)

    private val _result = MutableStateFlow<FiberLossCalculator.FiberLossResult?>(null)
    val result: StateFlow<FiberLossCalculator.FiberLossResult?> = _result

    fun calculate() {
        val dbm = inputDbm.value.toDoubleOrNull() ?: return
        val km = lengthKm.value.toDoubleOrNull() ?: return
        _result.value = FiberLossCalculator.calculate(
            inputDbm = dbm,
            wavelengthNm = wavelength.value,
            fiberLengthKm = km,
            connectorCount = connectors.value,
            spliceCount = splices.value
        )
    }
}
