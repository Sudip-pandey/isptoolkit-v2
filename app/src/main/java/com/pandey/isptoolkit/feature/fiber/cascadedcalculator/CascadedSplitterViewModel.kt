package com.pandey.isptoolkit.feature.fiber.cascadedcalculator

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.CascadedSplitterCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CascadedSplitterViewModel @Inject constructor() : ViewModel() {
    val inputDbm = MutableStateFlow("3.0")

    private val _stages = MutableStateFlow<List<CascadedSplitterCalculator.ChainStage>>(
        listOf(
            CascadedSplitterCalculator.ChainStage.FiberSpan(1.0, 1490),
            CascadedSplitterCalculator.ChainStage.Plc(8),
        )
    )
    val stages: StateFlow<List<CascadedSplitterCalculator.ChainStage>> = _stages

    private val _results = MutableStateFlow<List<CascadedSplitterCalculator.StageResult>>(emptyList())
    val results: StateFlow<List<CascadedSplitterCalculator.StageResult>> = _results

    fun calculate() {
        val dbm = inputDbm.value.toDoubleOrNull() ?: return
        _results.value = CascadedSplitterCalculator.calculate(dbm, _stages.value)
    }

    fun addFiberStage() {
        _stages.value = _stages.value + CascadedSplitterCalculator.ChainStage.FiberSpan(1.0, 1490)
    }

    fun addPlcStage() {
        _stages.value = _stages.value + CascadedSplitterCalculator.ChainStage.Plc(4)
    }

    fun removeLastStage() {
        if (_stages.value.isNotEmpty()) {
            _stages.value = _stages.value.dropLast(1)
        }
    }
}
