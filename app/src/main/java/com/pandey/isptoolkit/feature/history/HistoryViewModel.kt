package com.pandey.isptoolkit.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.data.local.dao.FiberCalculationDao
import com.pandey.isptoolkit.data.local.dao.SiteDao
import com.pandey.isptoolkit.data.local.entity.FiberCalculationEntity
import com.pandey.isptoolkit.data.local.entity.SiteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class BeforeAfterComparison(
    val siteName: String = "Customer Residence #102",
    val beforeRssiDbm: Int = -77,
    val afterRssiDbm: Int = -61,
    val beforeLatencyMs: Int = 45,
    val afterLatencyMs: Int = 18,
    val beforeOntRxDbm: Double = -29.2,
    val afterOntRxDbm: Double = -21.4
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val fiberCalculationDao: FiberCalculationDao,
    private val siteDao: SiteDao
) : ViewModel() {

    val calculationsFlow: StateFlow<List<FiberCalculationEntity>> = fiberCalculationDao.getAllCalculationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sitesFlow: StateFlow<List<SiteEntity>> = siteDao.getAllSitesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteCalculation(id: String) {
        viewModelScope.launch {
            fiberCalculationDao.deleteCalculation(id)
        }
    }

    fun createNewSite(name: String, ontModel: String, plan: String, ssid: String) {
        viewModelScope.launch {
            siteDao.insertSite(
                SiteEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    customerReference = "REF-${System.currentTimeMillis() % 10000}",
                    routerModel = "Wi-Fi 6 Router",
                    ontModel = ontModel,
                    internetPlan = plan,
                    ssid = ssid,
                    vlan = "100",
                    notes = "Field installation completed",
                    createdAtTimestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
