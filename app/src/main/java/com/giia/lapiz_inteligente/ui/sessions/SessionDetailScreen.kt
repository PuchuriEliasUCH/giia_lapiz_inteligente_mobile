package com.giia.lapiz_inteligente.ui.sessions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.giia.lapiz_inteligente.ui.dashboard.PostureCard
import com.giia.lapiz_inteligente.ui.dashboard.PressureCard
import com.giia.lapiz_inteligente.ui.dashboard.StabilityCard
import com.giia.lapiz_inteligente.ui.dashboard.TremorCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.giia.lapiz_inteligente.ui.theme.Primary

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
    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableStateOf(0) }

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
                    isRunning = false
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
                title = { Text("Sesión") },
                navigationIcon = {
                    IconButton(onClick = {
                        isRunning = false
                        onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
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
                        modifier = Modifier.fillMaxSize().padding(24.dp),
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
                    }
                }

                is ActiveSessionUiState.Success -> {
                    val session = state.session
                    val hasMetrics = session.avg_pressure != null || session.tremor_level != null

                    Column(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isRunning) "● En progreso" else "● No iniciada",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isRunning) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(224.dp)
                        ) {
                            val progress = if (isRunning) (elapsedSeconds % 60) / 60f else 0f
                            Canvas(modifier = Modifier.size(224.dp)) {
                                val strokeWidth = 8.dp.toPx()
                                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                                drawArc(
                                    color = Color(0xFFD9E3F6),
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )

                                if (isRunning) {
                                    drawArc(
                                        color = Primary,
                                        startAngle = -90f,
                                        sweepAngle = 360f * progress,
                                        useCenter = false,
                                        topLeft = topLeft,
                                        size = arcSize,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val mins = elapsedSeconds / 60
                                val secs = elapsedSeconds % 60
                                Text(
                                    text = String.format("%02d:%02d", mins, secs),
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                Text(
                                    text = "min:seg",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        if (hasMetrics) {
                            Text(
                                text = "Métricas",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PressureCard(
                                avgPressure = session.avg_pressure,
                                maxPressure = session.max_pressure
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            StabilityCard(
                                pressureStability = session.pressure_stability,
                                movementStability = session.movement_stability
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TremorCard(tremorLevel = session.tremor_level)
                            Spacer(modifier = Modifier.height(8.dp))
                            PostureCard(postureScore = session.posture_score)

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!isRunning) {
                            Button(
                                onClick = { isRunning = true },
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text("Iniciar Sesión")
                            }
                        } else {
                            Button(
                                onClick = { showEndDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text("Terminar Sesión")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
