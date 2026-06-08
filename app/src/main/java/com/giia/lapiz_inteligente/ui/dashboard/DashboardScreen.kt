package com.giia.lapiz_inteligente.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.giia.lapiz_inteligente.ui.sessions.SessionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSessionMetrics: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard de Métricas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val current = state) {
                is DashboardUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is DashboardUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = current.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDashboard() }) {
                            Text("Reintentar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }

                is DashboardUiState.Empty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No hay datos de sesiones aún.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDashboard() }) {
                            Text("Reintentar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }

                is DashboardUiState.Success -> {
                    DashboardContent(
                        summary = current.summary,
                        recentSessions = current.recentSessions,
                        onSessionClick = onNavigateToSessionMetrics,
                        onRefresh = { viewModel.loadDashboard() }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    summary: com.giia.lapiz_inteligente.models.dashboard.DashboardSummary,
    recentSessions: List<com.giia.lapiz_inteligente.models.session.SessionResponse>,
    onSessionClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            SummaryCard(summary)
        }

        item {
            PressureCard(
                avgPressure = summary.avgPressure,
                maxPressure = summary.maxPressure
            )
        }

        item {
            StabilityCard(
                pressureStability = summary.avgPressureStability,
                movementStability = summary.avgMovementStability
            )
        }

        item {
            TremorCard(tremorLevel = summary.avgTremorLevel)
        }

        item {
            PostureCard(postureScore = summary.avgPostureScore)
        }

        if (recentSessions.isNotEmpty()) {
            item {
                Text(
                    text = "Sesiones Recientes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(recentSessions, key = { it.session_id }) { session ->
                SessionCard(session = session)
            }
        }
    }
}

@Composable
private fun SummaryCard(summary: com.giia.lapiz_inteligente.models.dashboard.DashboardSummary) {
    val levelColor = when (summary.performanceLevel) {
        "Excelente" -> Color(0xFF2E7D32)
        "Bueno" -> Color(0xFFF57F17)
        "Regular" -> Color(0xFFF57F17)
        "Mejorable" -> Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Resumen General",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Nivel: ${summary.performanceLevel}",
                style = MaterialTheme.typography.headlineSmall,
                color = levelColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sesiones realizadas: ${summary.totalSessions}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (summary.recommendation != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = summary.recommendation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
