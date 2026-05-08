package com.jenugumpu.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jenugumpu.app.R
import com.jenugumpu.app.data.entity.FloralSource

@Composable
fun getFloralSourceString(source: FloralSource): String {
    return when (source) {
        FloralSource.COFFEE -> stringResource(R.string.floral_coffee)
        FloralSource.WILDFLOWER -> stringResource(R.string.floral_wildflower)
        FloralSource.NEEM -> stringResource(R.string.floral_neem)
        FloralSource.MIXED -> stringResource(R.string.floral_mixed)
    }
}

@Composable
fun getGradeString(grade: String): String {
    return when (grade) {
        "Light" -> stringResource(R.string.grade_light)
        "Medium" -> stringResource(R.string.grade_medium)
        "Dark" -> stringResource(R.string.grade_dark)
        else -> grade
    }
}

fun getGradeColor(grade: String): Color {
    return when (grade) {
        "Light" -> Color(0xFFFFD54F) // Amber 300
        "Medium" -> Color(0xFFFF9800) // Orange 500
        "Dark" -> Color(0xFF795548) // Brown 500
        else -> Color.Gray
    }
}
