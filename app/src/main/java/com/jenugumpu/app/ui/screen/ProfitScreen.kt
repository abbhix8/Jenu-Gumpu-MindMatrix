package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.ui.viewmodel.ProfitViewModel

/**
 * Profit Calculator Screen
 * Calculate profit based on cost and selling price
 */
@Composable
fun ProfitScreen(
    viewModel: ProfitViewModel
) {
    val quantity by viewModel.quantity.collectAsState()
    val costPerKg by viewModel.costPerKg.collectAsState()
    val sellingPricePerKg by viewModel.sellingPricePerKg.collectAsState()
    val filteringCostPerKg by viewModel.filteringCostPerKg.collectAsState()

    val totalCost by viewModel.totalCost.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalProfit by viewModel.totalProfit.collectAsState()
    val profitPerKg by viewModel.profitPerKg.collectAsState()
    val totalFilteringCost by viewModel.totalFilteringCost.collectAsState()
    val earningsAfterFiltering by viewModel.earningsAfterFiltering.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.profit_calculator),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // Market Trends Card (Price Monitor)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.market_trends),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = stringResource(R.string.retail_price),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.wholesale_price),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.market_advice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        // Input Form
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.enter_details),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { viewModel.quantity.value = it },
                    label = { Text(stringResource(R.string.quantity_kg)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = costPerKg,
                    onValueChange = { viewModel.costPerKg.value = it },
                    label = { Text(stringResource(R.string.cost_per_kg)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("₹ ") }
                )

                OutlinedTextField(
                    value = sellingPricePerKg,
                    onValueChange = { viewModel.sellingPricePerKg.value = it },
                    label = { Text(stringResource(R.string.selling_price_kg)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("₹ ") }
                )

                OutlinedTextField(
                    value = filteringCostPerKg,
                    onValueChange = { viewModel.filteringCostPerKg.value = it },
                    label = { Text(stringResource(R.string.filtering_cost_kg)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("₹ ") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.calculateProfit() },
                        modifier = Modifier.weight(1f),
                        enabled = quantity.toDoubleOrNull() != null &&
                                costPerKg.toDoubleOrNull() != null &&
                                sellingPricePerKg.toDoubleOrNull() != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.calculate))
                    }

                    OutlinedButton(
                        onClick = { viewModel.reset() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.reset))
                    }
                }
            }
        }

        // Results Section
        if (totalCost > 0 || totalRevenue > 0 || totalProfit != 0.0) {
            Text(
                text = stringResource(R.string.results),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Total Cost
            ResultCard(
                label = stringResource(R.string.total_cost),
                value = totalCost,
                color = MaterialTheme.colorScheme.errorContainer
            )

            // Total Revenue
            ResultCard(
                label = stringResource(R.string.total_revenue),
                value = totalRevenue,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            // Filtering Cost
            if (totalFilteringCost > 0) {
                ResultCard(
                    label = stringResource(R.string.filtering_cost_label),
                    value = totalFilteringCost,
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }

            // Earnings After Filtering
            if (totalFilteringCost > 0) {
                ResultCard(
                    label = stringResource(R.string.earnings_after_filtering),
                    value = earningsAfterFiltering,
                    color = if (earningsAfterFiltering >= 0)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer
                )
            }

            // Total Profit
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (totalProfit >= 0)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.total_profit),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹ ${String.format("%.2f", totalProfit)}",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (totalProfit >= 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${stringResource(R.string.profit_per_kg)} ₹ ${String.format("%.2f", profitPerKg)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Profit Margin
            if (totalRevenue > 0) {
                val marginPercentage = (totalProfit / totalRevenue) * 100
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
                            text = stringResource(R.string.profit_margin),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = String.format("%.1f%%", marginPercentage),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (marginPercentage >= 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    label: String,
    value: Double,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "₹ ${String.format("%.2f", value)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
