package com.jenugumpu.app.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.BatchStatus
import com.jenugumpu.app.data.entity.BatchWithLogs
import com.jenugumpu.app.data.entity.HarvestLog
import com.jenugumpu.app.ui.utils.getFloralSourceString
import com.jenugumpu.app.ui.utils.getGradeColor
import com.jenugumpu.app.ui.utils.getGradeString
import com.jenugumpu.app.ui.viewmodel.StockViewModel
import com.jenugumpu.app.utils.DateFormatter
import com.jenugumpu.app.utils.QRCodeGenerator
import com.jenugumpu.app.ui.utils.getFloralSourceString
import com.jenugumpu.app.ui.utils.getGradeColor
import com.jenugumpu.app.ui.utils.getGradeString


/**
 * Stock & Batch Management Screen
 * Create batches from harvest logs
 * Display existing batches with QR codes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(
    viewModel: StockViewModel
) {
    val unusedLogs by viewModel.unusedHarvestLogs.collectAsState()
    val batches by viewModel.batchesWithLogs.collectAsState()
    val selectedLogIds by viewModel.selectedLogIds.collectAsState()
    val batchGrade by viewModel.batchGrade.collectAsState()

    var showBatchDialog by remember { mutableStateOf(false) }
    var expandedGrade by remember { mutableStateOf(false) }
    var selectedBatch by remember { mutableStateOf<BatchWithLogs?>(null) }
    var showQRDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.batch_management),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Create Batch Section
        if (unusedLogs.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.create_new_batch),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.select_logs_include),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    unusedLogs.forEach { log ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleLogSelection(log.id) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedLogIds.contains(log.id),
                                onCheckedChange = { viewModel.toggleLogSelection(log.id) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${log.quantity} kg - ${getFloralSourceString(log.floralSource)} (${getGradeString(log.grade)})",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    if (selectedLogIds.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Grade Selection
                        ExposedDropdownMenuBox(
                            expanded = expandedGrade,
                            onExpandedChange = { expandedGrade = it }
                        ) {
                            OutlinedTextField(
                                value = getGradeString(batchGrade),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.batch_grade)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGrade) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedGrade,
                                onDismissRequest = { expandedGrade = false }
                            ) {
                                listOf("Light", "Medium", "Dark").forEach { gradeOption ->
                                    DropdownMenuItem(
                                        text = { Text(getGradeString(gradeOption)) },
                                        onClick = {
                                            viewModel.batchGrade.value = gradeOption
                                            expandedGrade = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.createBatch { batchId ->
                                    showBatchDialog = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(String.format(stringResource(R.string.create_batch_format), selectedLogIds.size))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Batches List
        Text(
            text = stringResource(R.string.existing_batches),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(batches, key = { it.batch.batchId }) { batchWithLogs ->
                BatchItem(
                    batchWithLogs = batchWithLogs,
                    onQRClick = {
                        selectedBatch = batchWithLogs
                        showQRDialog = true
                    },
                    onStatusChange = { newStatus ->
                        viewModel.updateBatchStatus(batchWithLogs.batch.batchId, newStatus)
                    }
                )
            }
        }
    }

    if (showBatchDialog) {
        AlertDialog(
            onDismissRequest = { showBatchDialog = false },
            title = { Text(stringResource(R.string.batch_created)) },
            text = { Text(stringResource(R.string.batch_created_msg)) },
            confirmButton = {
                TextButton(onClick = { showBatchDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    // QR Code Dialog
    if (showQRDialog && selectedBatch != null) {
        QRCodeDialog(
            batch = selectedBatch!!,
            onDismiss = {
                showQRDialog = false
                selectedBatch = null
            }
        )
    }
}

@Composable
fun BatchItem(
    batchWithLogs: BatchWithLogs,
    onQRClick: () -> Unit,
    onStatusChange: (BatchStatus) -> Unit
) {
    var expandedStatus by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = batchWithLogs.batch.batchId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${batchWithLogs.batch.totalQuantity} kg - ${getFloralSourceString(batchWithLogs.batch.floralSource)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${stringResource(R.string.grade)}: ${getGradeString(batchWithLogs.batch.grade)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(getGradeColor(batchWithLogs.batch.grade))
                        )
                    }
                    Text(
                        text = DateFormatter.formatDate(batchWithLogs.batch.createdDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onQRClick) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Show QR Code"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status Chip
            FilterChip(
                selected = true,
                onClick = { expandedStatus = true },
                label = { Text(batchWithLogs.batch.status.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            // Status Dropdown
            DropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false }
            ) {
                BatchStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name) },
                        onClick = {
                            onStatusChange(status)
                            expandedStatus = false
                        }
                    )
                }
            }

            // Logs count
            Text(
                text = String.format(stringResource(R.string.contains_logs), batchWithLogs.harvestLogs.size),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QRCodeDialog(
    batch: BatchWithLogs,
    onDismiss: () -> Unit
) {
    // Generate QR code
    val qrBitmap = remember(batch.batch.batchId) {
        QRCodeGenerator.generateBatchQRCode(
            batchId = batch.batch.batchId,
            floralSource = batch.batch.floralSource.name,
            grade = batch.batch.grade,
            quantity = batch.batch.totalQuantity,
            date = DateFormatter.formatDateForQR(batch.batch.createdDate),
            size = 512
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.batch_qr_code),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = batch.batch.batchId,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(300.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${batch.batch.totalQuantity} kg",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${getFloralSourceString(batch.batch.floralSource)} - ${getGradeString(batch.batch.grade)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    }
}
