package com.pandey.isptoolkit.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.data.discovery.LanDiscoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DevicesUiState(
    val isScanning: Boolean = false,
    val devices: List<LanDiscoveryRepository.DiscoveredDevice> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val lanRepo: LanDiscoveryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState: StateFlow<DevicesUiState> = _uiState

    fun scanLan(baseIp: String = "192.168.1.0") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true, devices = emptyList())
            val devices = lanRepo.scanSubnet(baseIp)
            _uiState.value = _uiState.value.copy(isScanning = false, devices = devices)
        }
    }
}
