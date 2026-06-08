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
fun PostureCard(
    postureScore: Double?,
    modifier: Modifier = Modifier
) {
    val displayValue = if (postureScore != null) String.format("%.0f", postureScore) else "—"
    val interpretation = when {
        postureScore == null -> "Sin datos"
        postureScore >= 80 -> "Excelente"
        postureScore >= 60 -> "Buena"
        else -> "Mejorable"
    }
    val color = when (interpretation) {
        "Excelente" -> Color(0xFF2E7D32)
        "Buena" -> Color(0xFFF57F17)
        "Mejorable" -> Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Postura",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$displayValue / 100",
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
