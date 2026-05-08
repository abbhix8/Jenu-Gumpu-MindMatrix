package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.FloralSource
import com.jenugumpu.app.data.entity.HarvestLog
import com.jenugumpu.app.ui.utils.getFloralSourceString
import com.jenugumpu.app.ui.utils.getGradeColor
import com.jenugumpu.app.ui.utils.getGradeString
import com.jenugumpu.app.ui.viewmodel.HarvestViewModel
import com.jenugumpu.app.utils.DateFormatter

/**
 * Harvest Screen - Add and view harvest logs
 * Form at top, list below
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarvestScreen(
    viewModel: HarvestViewModel
) {
    val quantity by viewModel.quantity.collectAsState()
    val selectedFloralSource by viewModel.selectedFloralSource.collectAsState()
    val grade by viewModel.grade.collectAsState()
    val location by viewModel.location.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val harvestLogs by viewModel.harvestLogs.collectAsState()
    val showSuccessMessage by viewModel.showSuccessMessage.collectAsState()

    var expandedFloralSource by remember { mutableStateOf(false) }
    var expandedGrade by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.log_harvest),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Form
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Quantity
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { viewModel.quantity.value = it },
                    label = { Text(stringResource(R.string.quantity_kg)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Floral Source Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedFloralSource,
                    onExpandedChange = { expandedFloralSource = it }
                ) {
                    OutlinedTextField(
                        value = getFloralSourceString(selectedFloralSource),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.floral_source)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFloralSource) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedFloralSource,
                        onDismissRequest = { expandedFloralSource = false }
                    ) {
                        FloralSource.values().forEach { source ->
                            DropdownMenuItem(
                                text = { Text(getFloralSourceString(source)) },
                                onClick = {
                                    viewModel.selectedFloralSource.value = source
                                    expandedFloralSource = false
                                }
                            )
                        }
                    }
                }

                // Grade Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedGrade,
                    onExpandedChange = { expandedGrade = it }
                ) {
                    OutlinedTextField(
                        value = getGradeString(grade),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.grade)) },
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
                                    viewModel.grade.value = gradeOption
                                    expandedGrade = false
                                }
                            )
                        }
                    }
                }

                // Location
                OutlinedTextField(
                    value = location,
                    onValueChange = { viewModel.location.value = it },
                    label = { Text(stringResource(R.string.location)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { viewModel.notes.value = it },
                    label = { Text(stringResource(R.string.notes_optional)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                // Submit Button
                Button(
                    onClick = { viewModel.addHarvestLog() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = quantity.toDoubleOrNull() != null && quantity.toDoubleOrNull()!! > 0
                ) {
                    Text(stringResource(R.string.add_harvest_log))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Harvest Logs List
        Text(
            text = stringResource(R.string.recent_harvests),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(harvestLogs, key = { it.id }) { log ->
                HarvestLogItem(
                    log = log,
                    onDelete = { viewModel.deleteHarvestLog(log) }
                )
            }
        }
    }

    // Success Snackbar
    if (showSuccessMessage) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            viewModel.dismissSuccessMessage()
        }
    }
}

@Composable
fun HarvestLogItem(
    log: HarvestLog,
    onDelete: () -> Unit
) {
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${log.quantity} kg - ${getFloralSourceString(log.floralSource)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (log.location.isNotBlank()) {
                    Text(
                        text = "${stringResource(R.string.location)}: ${log.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${stringResource(R.string.grade)}: ${getGradeString(log.grade)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(getGradeColor(log.grade))
                    )
                }
                Text(
                    text = DateFormatter.formatDate(log.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (log.notes.isNotBlank()) {
                    Text(
                        text = log.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (log.isUsedInBatch) {
                    Text(
                        text = stringResource(R.string.in_batch),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (!log.isUsedInBatch) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

