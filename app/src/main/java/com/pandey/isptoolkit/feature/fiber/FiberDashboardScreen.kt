package com.pandey.isptoolkit.feature.fiber

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pandey.isptoolkit.feature.fiber.cascadedcalculator.CascadedSplitterScreen
import com.pandey.isptoolkit.feature.fiber.couplercalculator.CouplerScreen
import com.pandey.isptoolkit.feature.fiber.fiberloss.FiberLossScreen
import com.pandey.isptoolkit.feature.fiber.linkbudget.LinkBudgetScreen
import com.pandey.isptoolkit.feature.fiber.ont.OntReadingScreen
import com.pandey.isptoolkit.feature.fiber.opticalpower.OpticalPowerScreen
import com.pandey.isptoolkit.feature.fiber.plccalculator.PlcSplitterScreen

@Composable
fun FiberDashboardScreen() {
    val tabs = listOf("Optical Power", "PLC Splitter", "Coupler", "Fiber Loss", "Link Budget", "ONT", "Cascaded")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTab) {
            0 -> OpticalPowerScreen()
            1 -> PlcSplitterScreen()
            2 -> CouplerScreen()
            3 -> FiberLossScreen()
            4 -> LinkBudgetScreen()
            5 -> OntReadingScreen()
            6 -> CascadedSplitterScreen()
        }
    }
}
