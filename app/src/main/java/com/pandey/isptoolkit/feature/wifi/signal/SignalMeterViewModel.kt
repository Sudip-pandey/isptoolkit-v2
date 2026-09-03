package com.pandey.isptoolkit.feature.wifi.signal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.data.repository.WifiAnalyzerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignalMeterUiState(
    val isRunning: Boolean = false,
    val currentDbm: Int = -100,
    val minDbm: Int = -100,
    val maxDbm: Int = -100,
    val avgDbm: Double = -100.0,
    val history: List<Int> = emptyList()
)

@HiltViewModel
class SignalMeterViewModel @Inject constructor(
    private val wifiRepo: WifiAnalyzerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalMeterUiState())
    val uiState: StateFlow<SignalMeterUiState> = _uiState

    fun startMonitoring() {
        _uiState.value = _uiState.value.copy(isRunning = true)
        viewModelScope.launch {
            while (_uiState.value.isRunning) {
                val aps = wifiRepo.getScanResults()
                val strongest = aps.firstOrNull()?.signalDbm ?: -100
                val history = (_uiState.value.history + strongest).takeLast(60)
                _uiState.value = _uiState.value.copy(
                    currentDbm = strongest,
                    minDbm = history.min(),
                    maxDbm = history.max(),
                    avgDbm = history.average(),
                    history = history
                )
                delay(2000)
            }
        }
    }

    fun stopMonitoring() {
        _uiState.value = _uiState.value.copy(isRunning = false)
    }
}
