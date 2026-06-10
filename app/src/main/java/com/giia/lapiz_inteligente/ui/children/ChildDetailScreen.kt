package com.giia.lapiz_inteligente.ui.children

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.giia.lapiz_inteligente.models.child.ChildResponse
import com.giia.lapiz_inteligente.models.dashboard.DashboardSummary
import com.giia.lapiz_inteligente.models.session.SessionResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailScreen(
    childId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToExercises: (childId: Int, exerciseId: Int) -> Unit,
    onNavigateToSessionDetail: (sessionId: Int) -> Unit,
    onStartNewSession: (childId: Int) -> Unit,
    viewModel: ChildDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(childId) {
        viewModel.loadChildDetail(childId)
    }

    when (val current = state) {
        is ChildDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ChildDetailState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = current.message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadChildDetail(childId) }) {
                    Text("Reintentar")
                }
            }
        }

        is ChildDetailState.Success -> {
            ChildDetailContent(
                child = current.child,
                sessions = current.sessions,
                metrics = current.metrics,
                onNavigateBack = onNavigateBack,
                onNavigateToEdit = onNavigateToEdit,
                onNavigateToExercises = onNavigateToExercises,
                onNavigateToSessionDetail = onNavigateToSessionDetail,
                onStartNewSession = onStartNewSession
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChildDetailContent(
    child: ChildResponse,
    sessions: List<SessionResponse>,
    metrics: DashboardSummary,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToExercises: (childId: Int, exerciseId: Int) -> Unit,
    onNavigateToSessionDetail: (sessionId: Int) -> Unit,
    onStartNewSession: (childId: Int) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Datos", "Sesiones", "Métricas")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(child.name, style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(title, style = MaterialTheme.typography.labelLarge)
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> ChildDataTab(child = child, sessionCount = sessions.size, onNavigateToEdit = onNavigateToEdit)
                1 -> ChildSessionsTab(
                    childId = child.child_id,
                    sessions = sessions,
                    onNavigateToExercises = onNavigateToExercises,
                    onNavigateToSessionDetail = onNavigateToSessionDetail,
                    onStartNewSession = onStartNewSession
                )
                2 -> ChildMetricsTab(metrics = metrics)
            }
        }
    }
}

@Composable
private fun ChildDataTab(
    child: ChildResponse,
    sessionCount: Int,
    onNavigateToEdit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())
    ) {
        CardData(label = "Nombre", value = child.name)
        Spacer(modifier = Modifier.height(12.dp))
        CardData(label = "ID", value = "#CH-${child.child_id.toString().padStart(4, '0')}")
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CardData(
                label = "Edad",
                value = child.birth_date?.let { calculateAge(it) } ?: "No registrada",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardData(
                label = "Mano",
                value = child.dominant_hand?.replaceFirstChar { it.uppercase() } ?: "No registrada",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CardData(
                label = "Grado",
                value = child.school_grade ?: "No registrado",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardData(
                label = "Sesiones",
                value = sessionCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        if (!child.notes.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Notas",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = child.notes,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToEdit,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Editar Datos")
        }
    }
}

@Composable
private fun CardData(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ChildSessionsTab(
    childId: Int,
    sessions: List<SessionResponse>,
    onNavigateToExercises: (childId: Int, exerciseId: Int) -> Unit,
    onNavigateToSessionDetail: (sessionId: Int) -> Unit,
    onStartNewSession: (childId: Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            Text(
                text = "Historial de Sesiones",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (sessions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No hay sesiones registradas.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sessions, key = { it.session_id }) { session ->
                        SessionCard(
                            session = session,
                            onClick = { onNavigateToSessionDetail(session.session_id) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { onStartNewSession(childId) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Text("+", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
private fun SessionCard(session: SessionResponse, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sesión #${session.session_id}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Inicio: ${session.started_at}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (session.ended_at != null) {
                Text(
                    text = "Fin: ${session.ended_at}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ChildMetricsTab(metrics: DashboardSummary) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            MetricCardData(
                label = "Puntaje Postura",
                value = metrics.avgPostureScore?.let { "${(it * 100).toInt()}%" } ?: "—",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            MetricCardData(
                label = "Estabilidad Mov.",
                value = metrics.avgMovementStability?.let { "${(it * 100).toInt()}%" } ?: "—",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            MetricCardData(
                label = "Presión Prom.",
                value = metrics.avgPressure?.let { "%.1f".format(it) } ?: "—",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            MetricCardData(
                label = "Temblor",
                value = metrics.avgTremorLevel?.let {
                    when {
                        it < 0.3 -> "Bajo"
                        it < 0.6 -> "Medio"
                        else -> "Alto"
                    }
                } ?: "—",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            MetricCardData(
                label = "Estabilidad Presión",
                value = metrics.avgPressureStability?.let { "${(it * 100).toInt()}%" } ?: "—",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            MetricCardData(
                label = "Rendimiento",
                value = metrics.performanceLevel,
                modifier = Modifier.weight(1f)
            )
        }

        if (!metrics.recommendation.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Recomendación",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = metrics.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun MetricCardData(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private fun calculateAge(birthDate: String): String {
    return try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val birth = sdf.parse(birthDate) ?: return "No registrada"
        val now = java.util.Date()
        val diff = now.time - birth.time
        val years = (diff / (1000L * 60 * 60 * 24 * 365.25)).toInt()
        if (years >= 0) "$years años" else "No registrada"
    } catch (_: Exception) {
        "No registrada"
    }
}
