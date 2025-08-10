package com.picka.expenseTracker.presentation.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.picka.expenseTracker.presentation.ui.screens.ExpenseEntryScreen
import com.picka.expenseTracker.presentation.ui.screens.ExpenseListScreen
import com.picka.expenseTracker.presentation.ui.screens.ReportsScreen
import com.picka.expenseTracker.presentation.ui.screens.SettingsScreen
import com.picka.expenseTracker.presentation.viewmodel.ExpenseViewModel
import com.picka.expenseTracker.presentation.viewmodel.ReportsViewModel
import com.picka.expenseTracker.presentation.viewmodel.ThemeViewModel


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Entry : Screen("entry", "Add", Icons.Default.Add)
    object List : Screen("list", "Expenses", Icons.Default.List)
    object Reports : Screen("reports", "Reports", Icons.Default.Assessment)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseNavigation(navController: NavHostController) {
    val screens = listOf(
        Screen.Entry,
        Screen.List,
        Screen.Reports,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
            navController,
            startDestination = Screen.Entry.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Entry.route) {
                val viewModel: ExpenseViewModel = hiltViewModel()
                ExpenseEntryScreen(viewModel = viewModel)
            }
            composable(Screen.List.route) {
                val viewModel: ExpenseViewModel = hiltViewModel()
                ExpenseListScreen(viewModel = viewModel)
            }
            composable(Screen.Reports.route) {
                val viewModel: ReportsViewModel = hiltViewModel()
                ReportsScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                val themeViewModel: ThemeViewModel = hiltViewModel()
                SettingsScreen(viewModel = themeViewModel)
            }
        }
    }
}
