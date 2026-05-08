package com.jenugumpu.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.app.R

/**
 * Sustainable Harvest Guidelines Screen
 * Provides tribal honey hunters with best practices
 * to harvest honey without killing the bee colony
 */
@Composable
fun SustainableHarvestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.sustainable_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.sustainable_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Guideline 1 - Timing
        GuidelineCard(
            icon = Icons.Default.Schedule,
            title = stringResource(R.string.guide_timing_title),
            description = stringResource(R.string.guide_timing_desc),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )

        // Guideline 2 - Leave honey for bees
        GuidelineCard(
            icon = Icons.Default.Favorite,
            title = stringResource(R.string.guide_leave_honey_title),
            description = stringResource(R.string.guide_leave_honey_desc),
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )

        // Guideline 3 - No smoke or fire
        GuidelineCard(
            icon = Icons.Default.SmokeFree,
            title = stringResource(R.string.guide_no_smoke_title),
            description = stringResource(R.string.guide_no_smoke_desc),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )

        // Guideline 4 - Protect the queen
        GuidelineCard(
            icon = Icons.Default.Shield,
            title = stringResource(R.string.guide_queen_title),
            description = stringResource(R.string.guide_queen_desc),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )

        // Guideline 5 - Night harvesting
        GuidelineCard(
            icon = Icons.Default.NightsStay,
            title = stringResource(R.string.guide_night_title),
            description = stringResource(R.string.guide_night_desc),
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )

        // Guideline 6 - Replanting
        GuidelineCard(
            icon = Icons.Default.Forest,
            title = stringResource(R.string.guide_replant_title),
            description = stringResource(R.string.guide_replant_desc),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )

        // Forest-to-Table badge
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
                Text(
                    text = stringResource(R.string.forest_to_table),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Text(
                    text = stringResource(R.string.forest_to_table_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }
}

@Composable
fun GuidelineCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
