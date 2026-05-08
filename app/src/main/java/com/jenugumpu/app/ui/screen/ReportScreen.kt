package com.jenugumpu.app.ui.screen

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.BatchWithLogs
import com.jenugumpu.app.data.entity.HarvestLog
import com.jenugumpu.app.ui.viewmodel.HarvestViewModel
import com.jenugumpu.app.ui.viewmodel.StockViewModel
import com.jenugumpu.app.utils.DateFormatter
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    harvestViewModel: HarvestViewModel,
    stockViewModel: StockViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val harvests by harvestViewModel.harvestLogs.collectAsState(initial = emptyList())
    val batches by stockViewModel.batchesWithLogs.collectAsState()
    val totalStock by stockViewModel.totalStock.collectAsState()

    var isGenerating by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.reports_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onLogout) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
            }
        }
        Text(
            text = stringResource(R.string.reports_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = stringResource(R.string.total_harvests),
                value = harvests.size.toString(),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = stringResource(R.string.total_batches),
                value = batches.size.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.total_collective_stock), style = MaterialTheme.typography.titleMedium)
                Text(
                    text = String.format("%.2f kg", totalStock),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isGenerating = true
                generateAndSaveReport(context, harvests, batches, totalStock)
                isGenerating = false
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isGenerating,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.generate_report))
        }

        if (isGenerating) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}

fun generateAndSaveReport(
    context: Context,
    harvests: List<HarvestLog>,
    batches: List<BatchWithLogs>,
    totalStock: Double
) {
    val timestamp = System.currentTimeMillis()
    val fileName = "JenuGumpu_Report_$timestamp.txt"
    
    val reportBuilder = StringBuilder()
    reportBuilder.append("========================================\n")
    reportBuilder.append("   JENU-GUMPU HONEY COLLECTIVE REPORT   \n")
    reportBuilder.append("========================================\n\n")
    reportBuilder.append("Date: ${DateFormatter.formatDate(timestamp)}\n")
    reportBuilder.append("Total Collective Stock: %.2f kg\n".format(totalStock))
    reportBuilder.append("Total Harvest Entries: ${harvests.size}\n")
    reportBuilder.append("Total Batches Created: ${batches.size}\n\n")

    reportBuilder.append("--- BATCH SUMMARY ---\n")
    batches.forEach { b ->
        reportBuilder.append("Batch ID: ${b.batch.batchId}\n")
        reportBuilder.append("  Quantity: ${b.batch.totalQuantity} kg\n")
        reportBuilder.append("  Source: ${b.batch.floralSource}\n")
        reportBuilder.append("  Grade: ${b.batch.grade}\n")
        reportBuilder.append("  Status: ${b.batch.status}\n")
        reportBuilder.append("  Created: ${DateFormatter.formatDate(b.batch.createdDate)}\n\n")
    }

    reportBuilder.append("--- HARVEST LOGS ---\n")
    harvests.forEach { h ->
        reportBuilder.append("${DateFormatter.formatDate(h.date)}: ${h.quantity} kg (${h.floralSource}) at ${h.location}\n")
    }

    reportBuilder.append("\nReport generated by Jenu-Gumpu App.")

    try {
        // Save to public Documents folder
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        FileOutputStream(file).use { 
            it.write(reportBuilder.toString().toByteArray())
        }
        Toast.makeText(context, "Report saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to save report: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
