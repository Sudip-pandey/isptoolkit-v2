package com.pandey.isptoolkit.feature.tools.subnet

import androidx.lifecycle.ViewModel
import com.pandey.isptoolkit.core.calculator.SubnetCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class SubnetUiState(
    val ipAddress: String = "192.168.1.1",
    val cidr: Int = 24,
    val result: SubnetCalculator.SubnetResult? = null,
    val error: String? = null
)

@HiltViewModel
class SubnetViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SubnetUiState())
    val uiState: StateFlow<SubnetUiState> = _uiState

    fun updateIp(ip: String) {
        _uiState.value = _uiState.value.copy(ipAddress = ip)
    }

    fun updateCidr(cidr: Int) {
        _uiState.value = _uiState.value.copy(cidr = cidr)
    }

    fun calculate() {
        val result = SubnetCalculator.calculate(_uiState.value.ipAddress, _uiState.value.cidr)
        _uiState.value = _uiState.value.copy(
            result = result,
            error = if (result == null) "Invalid IP address" else null
        )
    }
}
