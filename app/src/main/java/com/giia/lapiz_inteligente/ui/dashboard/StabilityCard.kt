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
fun StabilityCard(
    pressureStability: Double?,
    movementStability: Double?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Estabilidad",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            StabilityRow(
                label = "Presión",
                value = pressureStability,
                format = "%.2f"
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            StabilityRow(
                label = "Movimiento",
                value = movementStability,
                format = "%.2f"
            )
        }
    }
}

@Composable
private fun StabilityRow(label: String, value: Double?, format: String) {
    val displayValue = if (value != null) String.format(format, value) else "—"
    val interpretation = when {
        value == null -> "Sin datos"
        value >= 0.7 -> "Buena"
        value >= 0.4 -> "Regular"
        else -> "Baja"
    }
    val color = when (interpretation) {
        "Buena" -> Color(0xFF2E7D32)
        "Regular" -> Color(0xFFF57F17)
        "Baja" -> Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = displayValue,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = interpretation,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
