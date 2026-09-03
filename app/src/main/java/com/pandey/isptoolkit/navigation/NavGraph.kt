package com.pandey.isptoolkit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pandey.isptoolkit.feature.devices.DevicesScreen
import com.pandey.isptoolkit.feature.devices.DevicesViewModel
import com.pandey.isptoolkit.feature.fiber.FiberDashboardScreen
import com.pandey.isptoolkit.feature.fiber.cascadedcalculator.CascadedSplitterViewModel
import com.pandey.isptoolkit.feature.fiber.couplercalculator.CouplerViewModel
import com.pandey.isptoolkit.feature.fiber.fiberloss.FiberLossViewModel
import com.pandey.isptoolkit.feature.fiber.linkbudget.LinkBudgetViewModel
import com.pandey.isptoolkit.feature.fiber.ont.OntReadingViewModel
import com.pandey.isptoolkit.feature.fiber.opticalpower.OpticalPowerViewModel
import com.pandey.isptoolkit.feature.fiber.plccalculator.PlcSplitterViewModel
import com.pandey.isptoolkit.feature.history.HistoryScreen
import com.pandey.isptoolkit.feature.history.HistoryViewModel
import com.pandey.isptoolkit.feature.home.HomeScreen
import com.pandey.isptoolkit.feature.home.HomeViewModel
import com.pandey.isptoolkit.feature.home.customer.CustomerComplaintScreen
import com.pandey.isptoolkit.feature.home.customer.CustomerComplaintViewModel
import com.pandey.isptoolkit.feature.tools.pppoevlanmtu.PppoeVlanMtuToolsScreen
import com.pandey.isptoolkit.feature.tools.subnet.SubnetScreen
import com.pandey.isptoolkit.feature.tools.subnet.SubnetViewModel
import com.pandey.isptoolkit.feature.wifi.WifiScreen
import com.pandey.isptoolkit.feature.wifi.WifiViewModel
import com.pandey.isptoolkit.feature.wifi.signal.SignalMeterViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Devices : Screen("devices", "Devices", Icons.Default.Devices)
    object Wifi : Screen("wifi", "Wi-Fi", Icons.Default.Wifi)
    object Fiber : Screen("fiber", "Fiber", Icons.Default.Build)
    object Tools : Screen("tools", "Tools", Icons.Default.NetworkCheck)
    object History : Screen("history", "History", Icons.Default.History)
    object Complaint : Screen("complaint", "Complaint", Icons.Default.Home)
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Devices,
        Screen.Wifi,
        Screen.Fiber,
        Screen.Tools,
        Screen.History
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val homeVm: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = homeVm,
                    onNavigateToFiber = { navController.navigate(Screen.Fiber.route) },
                    onNavigateToWifi = { navController.navigate(Screen.Wifi.route) },
                    onNavigateToDevices = { navController.navigate(Screen.Devices.route) },
                    onNavigateToComplaint = { navController.navigate(Screen.Complaint.route) }
                )
            }
            composable(Screen.Devices.route) {
                val devicesVm: DevicesViewModel = hiltViewModel()
                DevicesScreen(viewModel = devicesVm)
            }
            composable(Screen.Wifi.route) {
                val wifiVm: WifiViewModel = hiltViewModel()
                val signalVm: SignalMeterViewModel = hiltViewModel()
                WifiScreen(wifiViewModel = wifiVm, signalMeterViewModel = signalVm)
            }
            composable(Screen.Fiber.route) {
                val opticalPowerVm: OpticalPowerViewModel = hiltViewModel()
                val plcVm: PlcSplitterViewModel = hiltViewModel()
                val couplerVm: CouplerViewModel = hiltViewModel()
                val cascadedVm: CascadedSplitterViewModel = hiltViewModel()
                val fiberLossVm: FiberLossViewModel = hiltViewModel()
                val linkBudgetVm: LinkBudgetViewModel = hiltViewModel()
                val ontVm: OntReadingViewModel = hiltViewModel()

                FiberDashboardScreen(
                    opticalPowerVm = opticalPowerVm,
                    plcVm = plcVm,
                    couplerVm = couplerVm,
                    cascadedVm = cascadedVm,
                    fiberLossVm = fiberLossVm,
                    linkBudgetVm = linkBudgetVm,
                    ontVm = ontVm
                )
            }
            composable(Screen.Tools.route) {
                val subnetVm: SubnetViewModel = hiltViewModel()
                SubnetScreen(viewModel = subnetVm)
            }
            composable(Screen.History.route) {
                val historyVm: HistoryViewModel = hiltViewModel()
                HistoryScreen(viewModel = historyVm)
            }
            composable(Screen.Complaint.route) {
                val complaintVm: CustomerComplaintViewModel = hiltViewModel()
                CustomerComplaintScreen(viewModel = complaintVm)
            }
        }
    }
}
