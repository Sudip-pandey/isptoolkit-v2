package com.pandey.isptoolkit.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.core.diagnostic.DiagnosticEngine
import com.pandey.isptoolkit.data.repository.NetworkInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val networkType: String = "Unknown",
    val isConnected: Boolean = false,
    val healthScore: Int = 0,
    val diagnosticSummary: String = "",
    val diagnosticResults: List<DiagnosticEngine.DiagnosticResult> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkInfoRepository: NetworkInfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadNetworkInfo()
    }

    fun loadNetworkInfo() {
        val info = networkInfoRepository.getNetworkInfo()
        _uiState.value = _uiState.value.copy(
            networkType = info.type,
            isConnected = info.isConnected
        )
    }

    fun runDiagnostics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val session = DiagnosticEngine.runFullDiagnostic()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    healthScore = session.healthScore,
                    diagnosticSummary = session.summary,
                    diagnosticResults = session.results
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
