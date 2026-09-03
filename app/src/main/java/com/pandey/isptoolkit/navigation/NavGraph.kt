package com.pandey.isptoolkit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pandey.isptoolkit.feature.devices.DevicesScreen
import com.pandey.isptoolkit.feature.fiber.FiberDashboardScreen
import com.pandey.isptoolkit.feature.history.HistoryScreen
import com.pandey.isptoolkit.feature.home.HomeScreen
import com.pandey.isptoolkit.feature.home.customer.CustomerComplaintScreen
import com.pandey.isptoolkit.feature.tools.pppoevlanmtu.PppoeVlanMtuToolsScreen
import com.pandey.isptoolkit.feature.tools.subnet.SubnetScreen
import com.pandey.isptoolkit.feature.wifi.WifiScreen
import com.pandey.isptoolkit.feature.wifi.signal.SignalMeterScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Fiber : Screen("fiber", "Fiber", Icons.Default.Cable)
    object Wifi : Screen("wifi", "Wi-Fi", Icons.Default.Wifi)
    object Devices : Screen("devices", "Devices", Icons.Default.Devices)
    object Tools : Screen("tools", "Tools", Icons.Default.Build)
    object History : Screen("history", "History", Icons.Default.History)
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        Screen.Home, Screen.Fiber, Screen.Wifi, Screen.Devices, Screen.Tools, Screen.History
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                HomeScreen(onOpenComplaint = { navController.navigate("complaint") })
            }
            composable("complaint") { CustomerComplaintScreen() }
            composable(Screen.Fiber.route) { FiberDashboardScreen() }
            composable(Screen.Wifi.route) {
                var showSignalMeter by remember { mutableStateOf(false) }
                if (showSignalMeter) {
                    SignalMeterScreen()
                } else {
                    WifiScreen()
                }
            }
            composable(Screen.Devices.route) { DevicesScreen() }
            composable(Screen.Tools.route) {
                var showSubnet by remember { mutableStateOf(true) }
                if (showSubnet) SubnetScreen() else PppoeVlanMtuToolsScreen()
            }
            composable(Screen.History.route) { HistoryScreen() }
        }
    }
}
