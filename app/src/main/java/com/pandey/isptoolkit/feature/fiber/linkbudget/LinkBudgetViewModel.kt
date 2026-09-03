package com.pandey.isptoolkit.feature.fiber.linkbudget

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.LinkBudgetCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LinkBudgetViewModel @Inject constructor() : ViewModel() {
    val txPower = MutableStateFlow("5.0")
    val totalLoss = MutableStateFlow("25.0")
    val rxSensitivity = MutableStateFlow("-28.0")
    val overload = MutableStateFlow("0.0")

    private val _result = MutableStateFlow<LinkBudgetCalculator.LinkBudgetResult?>(null)
    val result: StateFlow<LinkBudgetCalculator.LinkBudgetResult?> = _result

    fun calculate() {
        val tx = txPower.value.toDoubleOrNull() ?: return
        val loss = totalLoss.value.toDoubleOrNull() ?: return
        val rx = rxSensitivity.value.toDoubleOrNull() ?: return
        val ol = overload.value.toDoubleOrNull() ?: 0.0
        _result.value = LinkBudgetCalculator.calculate(tx, loss, rx, ol)
    }
}
