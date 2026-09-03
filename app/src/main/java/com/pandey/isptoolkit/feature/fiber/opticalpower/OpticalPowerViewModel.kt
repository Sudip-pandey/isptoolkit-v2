package com.pandey.isptoolkit.feature.fiber.opticalpower

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.OpticalPowerCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OpticalPowerViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableStateFlow<OpticalPowerCalculator.OpticalPowerResult?>(null)
    val result: StateFlow<OpticalPowerCalculator.OpticalPowerResult?> = _result

    private val _inputDbm = MutableStateFlow("-20.0")
    val inputDbm: StateFlow<String> = _inputDbm

    fun updateInput(value: String) { _inputDbm.value = value }

    fun calculate() {
        val dbm = _inputDbm.value.toDoubleOrNull() ?: return
        _result.value = OpticalPowerCalculator.calculate(dbm)
    }
}
