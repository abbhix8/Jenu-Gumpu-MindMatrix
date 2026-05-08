package com.jenugumpu.app.navigation

/**
 * Sealed class for type-safe navigation
 * Each screen has a unique route
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Harvest : Screen("harvest")
    object Stock : Screen("stock")
    object Camera : Screen("camera")
    object QRScanner : Screen("qr_scanner")
    object Profit : Screen("profit")
    object GradingGuide : Screen("grading_guide")
    object BatchComparison : Screen("batch_comparison")
    object SustainableHarvest : Screen("sustainable_harvest")
    object PriceMonitor : Screen("price_monitor")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object BuyerHome : Screen("buyer_home")
    object Reports : Screen("reports")
    object BatchDetail : Screen("batch_detail/{batchId}") {
        fun createRoute(batchId: String) = "batch_detail/$batchId"
    }
}
