package com.pandey.isptoolkit.feature.wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.data.repository.WifiAnalyzerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WifiUiState(
    val isScanning: Boolean = false,
    val accessPoints: List<WifiAnalyzerRepository.AccessPoint> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class WifiViewModel @Inject constructor(
    private val wifiRepo: WifiAnalyzerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WifiUiState())
    val uiState: StateFlow<WifiUiState> = _uiState

    fun scan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            delay(500)
            val results = wifiRepo.getScanResults()
            _uiState.value = _uiState.value.copy(
                isScanning = false,
                accessPoints = results
            )
        }
    }
}
