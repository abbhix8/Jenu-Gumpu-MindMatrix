package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.FloralSource
import com.jenugumpu.app.ui.viewmodel.StockViewModel

/**
 * Home Screen - Dashboard view
 * Shows total stock and quick action buttons
 */
@Composable
fun HomeScreen(
    stockViewModel: StockViewModel,
    onNavigateToHarvest: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToProfit: () -> Unit,
    onNavigateToGradingGuide: () -> Unit,
    onNavigateToBatchComparison: () -> Unit,
    onNavigateToSustainableHarvest: () -> Unit,
    onNavigateToPriceMonitor: () -> Unit,
    onLogout: () -> Unit
) {
    val totalStock by stockViewModel.totalStock.collectAsState()
    val coffeeStock by stockViewModel.coffeeStock.collectAsState()
    val wildflowerStock by stockViewModel.wildflowerStock.collectAsState()
    val neemStock by stockViewModel.neemStock.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onLogout) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
            }
        }

        Text(
            text = stringResource(R.string.app_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Total Stock Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.total_collective_stock),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format("%.2f kg", totalStock),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Stock Breakdown
        Text(
            text = stringResource(R.string.stock_by_floral_source),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        StockBreakdownItem(stringResource(R.string.floral_coffee), coffeeStock)
        StockBreakdownItem(stringResource(R.string.floral_wildflower), wildflowerStock)
        StockBreakdownItem(stringResource(R.string.floral_neem), neemStock)

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Actions
        Text(
            text = stringResource(R.string.quick_actions),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        QuickActionButton(
            icon = Icons.Default.Add,
            text = stringResource(R.string.log_harvest),
            onClick = onNavigateToHarvest
        )

        QuickActionButton(
            icon = Icons.Default.CameraAlt,
            text = stringResource(R.string.ai_grade_check),
            onClick = onNavigateToCamera
        )

        QuickActionButton(
            icon = Icons.Default.Inventory,
            text = stringResource(R.string.manage_batches),
            onClick = onNavigateToStock
        )

        QuickActionButton(
            icon = Icons.Default.QrCodeScanner,
            text = stringResource(R.string.scan_qr_code),
            onClick = onNavigateToScanner
        )

        QuickActionButton(
            icon = Icons.Default.Calculate,
            text = stringResource(R.string.profit_calculator),
            onClick = onNavigateToProfit
        )

        QuickActionButton(
            icon = Icons.Default.Star,
            text = stringResource(R.string.grading_guide),
            onClick = onNavigateToGradingGuide
        )

        QuickActionButton(
            icon = Icons.Default.CompareArrows,
            text = stringResource(R.string.compare_batches),
            onClick = onNavigateToBatchComparison
        )

        QuickActionButton(
            icon = Icons.Default.ShowChart,
            text = stringResource(R.string.price_monitor_title),
            onClick = onNavigateToPriceMonitor
        )

        QuickActionButton(
            icon = Icons.Default.Eco,
            text = stringResource(R.string.sustainable_harvest),
            onClick = onNavigateToSustainableHarvest
        )
    }
}

@Composable
fun StockBreakdownItem(source: String, quantity: Double) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = source,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = String.format("%.2f kg", quantity),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
