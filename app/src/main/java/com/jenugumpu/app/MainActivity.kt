package com.jenugumpu.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jenugumpu.app.data.database.JenuGumpuDatabase
import com.jenugumpu.app.data.repository.HoneyRepository
import com.jenugumpu.app.navigation.Screen
import com.jenugumpu.app.ui.screen.*
import com.jenugumpu.app.ui.theme.JenuGumpuTheme
import com.jenugumpu.app.ui.viewmodel.*
import com.jenugumpu.app.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = JenuGumpuDatabase.getDatabase(applicationContext)
        val repository = HoneyRepository(
            harvestLogDao = database.harvestLogDao(),
            batchDao = database.batchDao()
        )

        val viewModelFactory = ViewModelFactory(repository)

        setContent {
            JenuGumpuTheme {
                JenuGumpuApp(viewModelFactory)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JenuGumpuApp(
    viewModelFactory: ViewModelFactory
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val navController = rememberNavController()
    
    // Auth state
    var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }
    var userRole by remember { mutableStateOf(sessionManager.getUserRole()) }

    // ViewModels
    val harvestViewModel: HarvestViewModel = viewModel(factory = viewModelFactory)
    val stockViewModel: StockViewModel = viewModel(factory = viewModelFactory)
    val cameraViewModel: CameraViewModel = viewModel(factory = viewModelFactory)
    val qrScannerViewModel: QRScannerViewModel = viewModel(factory = viewModelFactory)
    val profitViewModel: ProfitViewModel = viewModel(factory = viewModelFactory)

    val startDestination = if (!isLoggedIn) Screen.Login.route 
                          else if (userRole == SessionManager.ROLE_BUYER) Screen.BuyerHome.route 
                          else Screen.Home.route

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Harvest,
        BottomNavItem.Stock,
        BottomNavItem.Reports,
        BottomNavItem.More
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = isLoggedIn && userRole == SessionManager.ROLE_COLLECTOR && 
                       bottomNavItems.any { it.screen.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues(0.dp))
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { username, role ->
                        sessionManager.saveSession(username, role)
                        isLoggedIn = true
                        userRole = role
                        navController.navigate(if (role == SessionManager.ROLE_BUYER) Screen.BuyerHome.route else Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
                )
            }

            composable(Screen.Signup.route) {
                SignupScreen(
                    onSignupSuccess = { username, role ->
                        sessionManager.saveSession(username, role)
                        isLoggedIn = true
                        userRole = role
                        navController.navigate(if (role == SessionManager.ROLE_BUYER) Screen.BuyerHome.route else Screen.Home.route) {
                            popUpTo(Screen.Signup.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.BuyerHome.route) {
                BuyerHomeScreen(
                    stockViewModel = stockViewModel,
                    onLogout = {
                        sessionManager.logout()
                        isLoggedIn = false
                        userRole = null
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    stockViewModel = stockViewModel,
                    onNavigateToHarvest = { navController.navigate(Screen.Harvest.route) },
                    onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                    onNavigateToStock = { navController.navigate(Screen.Stock.route) },
                    onNavigateToScanner = { navController.navigate(Screen.QRScanner.route) },
                    onNavigateToProfit = { navController.navigate(Screen.Profit.route) },
                    onNavigateToGradingGuide = { navController.navigate(Screen.GradingGuide.route) },
                    onNavigateToBatchComparison = { navController.navigate(Screen.BatchComparison.route) },
                    onNavigateToSustainableHarvest = { navController.navigate(Screen.SustainableHarvest.route) },
                    onNavigateToPriceMonitor = { navController.navigate(Screen.PriceMonitor.route) },
                    onLogout = {
                        sessionManager.logout()
                        isLoggedIn = false
                        userRole = null
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Harvest.route) {
                HarvestScreen(viewModel = harvestViewModel)
            }

            composable(Screen.Stock.route) {
                StockScreen(viewModel = stockViewModel)
            }

            composable(Screen.Camera.route) {
                CameraScreen(viewModel = cameraViewModel)
            }

            composable(Screen.QRScanner.route) {
                QRScannerScreen(viewModel = qrScannerViewModel)
            }

            composable(Screen.Profit.route) {
                ProfitScreen(viewModel = profitViewModel)
            }

            composable(Screen.GradingGuide.route) {
                GradingGuideScreen()
            }

            composable(Screen.BatchComparison.route) {
                BatchComparisonScreen(viewModel = stockViewModel)
            }

            composable(Screen.SustainableHarvest.route) {
                SustainableHarvestScreen()
            }

            composable(Screen.PriceMonitor.route) {
                PriceMonitorScreen()
            }

            composable(Screen.Reports.route) {
                ReportScreen(
                    harvestViewModel = harvestViewModel,
                    stockViewModel = stockViewModel,
                    onLogout = {
                        sessionManager.logout()
                        isLoggedIn = false
                        userRole = null
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(screen = Screen.Home, title = "Home", icon = Icons.Default.Home)
    object Harvest : BottomNavItem(screen = Screen.Harvest, title = "Harvest", icon = Icons.Default.Add)
    object Stock : BottomNavItem(screen = Screen.Stock, title = "Batches", icon = Icons.Default.Inventory)
    object Reports : BottomNavItem(screen = Screen.Reports, title = "Reports", icon = Icons.Default.Description)
    object More : BottomNavItem(screen = Screen.Camera, title = "AI Check", icon = Icons.Default.AutoAwesome)
}
