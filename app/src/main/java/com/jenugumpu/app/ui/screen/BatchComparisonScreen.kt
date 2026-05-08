package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.BatchWithLogs
import com.jenugumpu.app.ui.utils.getFloralSourceString
import com.jenugumpu.app.ui.utils.getGradeColor
import com.jenugumpu.app.ui.utils.getGradeString
import com.jenugumpu.app.ui.viewmodel.StockViewModel
import com.jenugumpu.app.utils.DateFormatter

/**
 * Batch Comparison Screen
 * Compares revenue/profit potential across different batches of honey
 * Shows estimated value based on market prices for each batch
 */
@Composable
fun BatchComparisonScreen(
    viewModel: StockViewModel
) {
    val batches by viewModel.batchesWithLogs.collectAsState()

    // Simulated market prices per kg by grade
    val priceByGrade = mapOf(
        "Light" to 500.0,
        "Medium" to 400.0,
        "Dark" to 350.0
    )

    val wholesalePrice = 150.0 // Middleman price per kg

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.batch_comparison_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.batch_comparison_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (batches.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_batches_yet),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Summary card
            val totalRetailValue = batches.sumOf { batch ->
                val price = priceByGrade[batch.batch.grade] ?: 350.0
                batch.batch.totalQuantity * price
            }
            val totalWholesaleValue = batches.sumOf { it.batch.totalQuantity * wholesalePrice }
            val totalQuantity = batches.sumOf { it.batch.totalQuantity }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.total_collective_value),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("%.1f kg", totalQuantity),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.retail_value),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = String.format("₹ %.0f", totalRetailValue),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = stringResource(R.string.wholesale_value),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = String.format("₹ %.0f", totalWholesaleValue),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    HorizontalDivider()

                    Text(
                        text = String.format(
                            stringResource(R.string.you_save),
                            totalRetailValue - totalWholesaleValue
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Individual batch comparison cards
            batches.forEach { batchWithLogs ->
                val retailPrice = priceByGrade[batchWithLogs.batch.grade] ?: 350.0
                val retailValue = batchWithLogs.batch.totalQuantity * retailPrice
                val wholesaleValue = batchWithLogs.batch.totalQuantity * wholesalePrice
                val savings = retailValue - wholesaleValue

                BatchComparisonCard(
                    batchWithLogs = batchWithLogs,
                    retailPrice = retailPrice,
                    retailValue = retailValue,
                    wholesaleValue = wholesaleValue,
                    savings = savings
                )
            }
        }
    }
}

@Composable
fun BatchComparisonCard(
    batchWithLogs: BatchWithLogs,
    retailPrice: Double,
    retailValue: Double,
    wholesaleValue: Double,
    savings: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(getGradeColor(batchWithLogs.batch.grade))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = batchWithLogs.batch.batchId,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${getFloralSourceString(batchWithLogs.batch.floralSource)} · ${getGradeString(batchWithLogs.batch.grade)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = String.format("%.1f kg", batchWithLogs.batch.totalQuantity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider()

            // Price comparison
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.retail_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("₹ %.0f/kg", retailPrice),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = String.format("₹ %.0f", retailValue),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.wholesale_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹ 150/kg",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = String.format("₹ %.0f", wholesaleValue),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Savings row
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format(
                            stringResource(R.string.extra_earnings),
                            savings
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = DateFormatter.formatDate(batchWithLogs.batch.createdDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
