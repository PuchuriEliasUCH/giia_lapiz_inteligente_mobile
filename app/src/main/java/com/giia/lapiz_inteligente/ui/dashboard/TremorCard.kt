package com.giia.lapiz_inteligente.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TremorCard(
    tremorLevel: Double?,
    modifier: Modifier = Modifier
) {
    val displayValue = if (tremorLevel != null) String.format("%.2f", tremorLevel) else "—"
    val interpretation = when {
        tremorLevel == null -> "Sin datos"
        tremorLevel < 0.3 -> "Bajo"
        tremorLevel < 0.6 -> "Medio"
        else -> "Alto"
    }
    val color = when (interpretation) {
        "Bajo" -> Color(0xFF2E7D32)
        "Medio" -> Color(0xFFF57F17)
        "Alto" -> Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Temblor",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = displayValue,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = interpretation,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}
