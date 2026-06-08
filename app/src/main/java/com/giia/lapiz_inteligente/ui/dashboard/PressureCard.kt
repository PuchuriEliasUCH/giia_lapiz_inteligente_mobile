package com.giia.lapiz_inteligente.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PressureCard(
    avgPressure: Double?,
    maxPressure: Double?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Presión",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            MetricRow(
                label = "Promedio",
                value = if (avgPressure != null) String.format("%.1f", avgPressure) else "—",
                interpretation = interpretPressure(avgPressure)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            MetricRow(
                label = "Máxima",
                value = if (maxPressure != null) String.format("%.1f", maxPressure) else "—",
                interpretation = interpretPressure(maxPressure)
            )
        }
    }
}

private fun interpretPressure(value: Double?): String = when {
    value == null -> "Sin datos"
    value < 30.0 -> "Baja"
    value < 60.0 -> "Normal"
    else -> "Alta"
}

@Composable
private fun MetricRow(label: String, value: String, interpretation: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        val color = when (interpretation) {
            "Normal", "Sin datos" -> MaterialTheme.colorScheme.onSurfaceVariant
            "Baja", "Alta" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Text(
            text = interpretation,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
