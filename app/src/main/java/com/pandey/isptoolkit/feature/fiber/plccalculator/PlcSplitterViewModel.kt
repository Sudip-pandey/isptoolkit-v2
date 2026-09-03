package com.pandey.isptoolkit.feature.fiber.plccalculator

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.PlcSplitterCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlcSplitterViewModel @Inject constructor() : ViewModel() {
    val inputDbm = MutableStateFlow("-3.0")
    val splitRatio = MutableStateFlow(4)
    private val _result = MutableStateFlow<PlcSplitterCalculator.SplitterResult?>(null)
    val result: StateFlow<PlcSplitterCalculator.SplitterResult?> = _result

    fun calculate() {
        val dbm = inputDbm.value.toDoubleOrNull() ?: return
        _result.value = PlcSplitterCalculator.calculate(dbm, splitRatio.value)
    }
}
