package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.BatchWithLogs
import com.jenugumpu.app.data.entity.FloralSource
import com.jenugumpu.app.ui.utils.getFloralSourceString
import com.jenugumpu.app.ui.utils.getGradeColor
import com.jenugumpu.app.ui.utils.getGradeString
import com.jenugumpu.app.ui.viewmodel.StockViewModel
import com.jenugumpu.app.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerHomeScreen(
    stockViewModel: StockViewModel,
    onLogout: () -> Unit
) {
    val batches by stockViewModel.batchesWithLogs.collectAsState()
    var selectedFilter by remember { mutableStateOf<FloralSource?>(null) }
    var showPurchaseDialog by remember { mutableStateOf(false) }

    val filteredBatches = if (selectedFilter == null) {
        batches
    } else {
        batches.filter { it.batch.floralSource == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(stringResource(R.string.marketplace_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.marketplace_subtitle), style = MaterialTheme.typography.bodySmall)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text(stringResource(R.string.all_sources)) }
                )
                FloralSource.values().forEach { source ->
                    FilterChip(
                        selected = selectedFilter == source,
                        onClick = { selectedFilter = source },
                        label = { Text(getFloralSourceString(source)) }
                    )
                }
            }

            if (filteredBatches.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_batches_available), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredBatches) { batchWithLogs ->
                        BuyerBatchItem(
                            batchWithLogs = batchWithLogs,
                            onBuyClick = { showPurchaseDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showPurchaseDialog) {
        AlertDialog(
            onDismissRequest = { showPurchaseDialog = false },
            title = { Text(stringResource(R.string.batch_details)) },
            text = { Text(stringResource(R.string.purchased_msg)) },
            confirmButton = {
                Button(onClick = { showPurchaseDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
fun BuyerBatchItem(
    batchWithLogs: BatchWithLogs,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = batchWithLogs.batch.batchId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${batchWithLogs.batch.totalQuantity} kg",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = getFloralSourceString(batchWithLogs.batch.floralSource),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(getGradeColor(batchWithLogs.batch.grade))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${stringResource(R.string.grade)}: ${getGradeString(batchWithLogs.batch.grade)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Harvested on: ${DateFormatter.formatDate(batchWithLogs.batch.createdDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBuyClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.buy_now))
            }
        }
    }
}
