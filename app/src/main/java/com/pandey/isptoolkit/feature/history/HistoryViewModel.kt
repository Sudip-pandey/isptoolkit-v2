package com.pandey.isptoolkit.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandey.isptoolkit.data.local.dao.FiberCalculationDao
import com.pandey.isptoolkit.data.local.entity.FiberCalculationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val fiberCalcDao: FiberCalculationDao
) : ViewModel() {

    val calculations: StateFlow<List<FiberCalculationEntity>> = fiberCalcDao.getCalculations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
