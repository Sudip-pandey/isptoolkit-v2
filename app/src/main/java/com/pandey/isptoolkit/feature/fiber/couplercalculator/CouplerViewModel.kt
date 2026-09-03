package com.pandey.isptoolkit.feature.fiber.couplercalculator

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.CouplerCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CouplerViewModel @Inject constructor() : ViewModel() {
    val inputDbm = MutableStateFlow("-3.0")
    val portAPercent = MutableStateFlow(50.0)
    private val _result = MutableStateFlow<CouplerCalculator.CouplerResult?>(null)
    val result: StateFlow<CouplerCalculator.CouplerResult?> = _result

    fun calculate() {
        val dbm = inputDbm.value.toDoubleOrNull() ?: return
        _result.value = CouplerCalculator.calculate(dbm, portAPercent.value)
    }
}
