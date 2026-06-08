package com.giia.lapiz_inteligente.ui.sessions

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.giia.lapiz_inteligente.ui.dashboard.PostureCard
import com.giia.lapiz_inteligente.ui.dashboard.PressureCard
import com.giia.lapiz_inteligente.ui.dashboard.StabilityCard
import com.giia.lapiz_inteligente.ui.dashboard.TremorCard

/**
 * Pantalla de detalle de una sesión activa.
 *
 * Muestra la información de la sesión en curso y permite finalizarla.
 *
 * @param sessionId Identificador de la sesión a mostrar.
 * @param onNavigateBack Callback para regresar.
 * @param onSessionEnded Callback al finalizar la sesión.
 * @param viewModel ViewModel inyectado por Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    sessionId: Int,
    onNavigateBack: () -> Unit,
    onSessionEnded: () -> Unit,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val activeState by viewModel.activeSessionState.collectAsState()
    var showEndDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sessionId) {
        viewModel.loadActiveSession(sessionId)
    }

    if (showEndDialog) {
        AlertDialog(
            onDismissRequest = { showEndDialog = false },
            title = { Text("Finalizar sesión") },
            text = { Text("¿Estás seguro de finalizar esta sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showEndDialog = false
                    viewModel.endSession(onSuccess = onSessionEnded)
                }) {
                    Text("Finalizar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sesión Activa") },
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
            when (val state = activeState) {
                null, is ActiveSessionUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ActiveSessionUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadActiveSession(sessionId) }) {
                            Text("Reintentar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Volver")
                        }
                    }
                }

                is ActiveSessionUiState.Success -> {
                    val session = state.session
                    val isActive = session.ended_at == null
                    val hasMetrics = session.avg_pressure != null || session.tremor_level != null

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            Text(
                                text = "Sesión #${session.session_id}",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            InfoRow("Estado", if (isActive) "Activa" else "Finalizada")
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow("Inicio", session.started_at.take(16).replace("T", " "))

                            if (!session.ended_at.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                InfoRow("Fin", session.ended_at.take(16).replace("T", " "))
                            }

                            if (hasMetrics) {
                                Spacer(modifier = Modifier.height(8.dp))
                                InfoRow("Puntaje IA",
                                    if (session.ai_score != null) String.format("%.1f", session.ai_score) else "—")
                            }
                        }

                        if (hasMetrics) {
                            item {
                                Text(
                                    text = "Métricas",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            item {
                                PressureCard(
                                    avgPressure = session.avg_pressure,
                                    maxPressure = session.max_pressure
                                )
                            }

                            item {
                                StabilityCard(
                                    pressureStability = session.pressure_stability,
                                    movementStability = session.movement_stability
                                )
                            }

                            item {
                                TremorCard(tremorLevel = session.tremor_level)
                            }

                            item {
                                PostureCard(postureScore = session.posture_score)
                            }

                            if (!session.result_summary.isNullOrBlank()) {
                                item {
                                    Text(
                                        text = session.result_summary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isActive) {
                                Button(
                                    onClick = { showEndDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Text("Finalizar sesión")
                                }
                            } else {
                                Button(
                                    onClick = onNavigateBack,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Text("Volver")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
