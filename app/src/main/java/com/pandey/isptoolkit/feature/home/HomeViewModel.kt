package com.pandey.isptoolkit.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.core.diagnostic.DiagnosticEngine
import com.pandey.isptoolkit.core.diagnostic.FullDiagnosticReport
import com.pandey.isptoolkit.data.local.dao.SiteDao
import com.pandey.isptoolkit.data.local.dao.VisitDao
import com.pandey.isptoolkit.data.local.entity.SiteEntity
import com.pandey.isptoolkit.data.local.entity.VisitEntity
import com.pandey.isptoolkit.data.repository.DetailedNetworkState
import com.pandey.isptoolkit.data.repository.NetworkInfoRepository
import com.pandey.isptoolkit.feature.speedtest.SpeedTestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val networkState: DetailedNetworkState? = null,
    val isRunningDiagnostics: Boolean = false,
    val diagnosticReport: FullDiagnosticReport? = null,
    val lastPingMs: String = "--",
    val downloadSpeedMbps: String = "--",
    val uploadSpeedMbps: String = "--"
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkInfoRepository: NetworkInfoRepository,
    private val diagnosticEngine: DiagnosticEngine,
    private val speedTestManager: SpeedTestManager,
    private val siteDao: SiteDao,
    private val visitDao: VisitDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val sitesFlow: StateFlow<List<SiteEntity>> = siteDao.getAllSitesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshNetworkState()
    }

    fun refreshNetworkState() {
        val state = networkInfoRepository.fetchCurrentNetworkState()
        _uiState.value = _uiState.value.copy(networkState = state)
    }

    fun runQuickDiagnostics() {
        _uiState.value = _uiState.value.copy(isRunningDiagnostics = true)
        viewModelScope.launch {
            val report = diagnosticEngine.runFullDiagnostics()
            _uiState.value = _uiState.value.copy(
                isRunningDiagnostics = false,
                diagnosticReport = report
            )
        }
    }

    fun runSpeedTest() {
        viewModelScope.launch {
            val res = speedTestManager.runFullSpeedTest("https://speed.cloudflare.com/__down?bytes=10000000")
            _uiState.value = _uiState.value.copy(
                lastPingMs = "${res.latencyMs.toInt()} ms",
                downloadSpeedMbps = "${String.format("%.1f", res.downloadMbps)} Mbps",
                uploadSpeedMbps = "${String.format("%.1f", res.uploadMbps)} Mbps"
            )
        }
    }
}
