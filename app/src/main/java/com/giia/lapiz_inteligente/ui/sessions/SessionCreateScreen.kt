package com.giia.lapiz_inteligente.ui.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCreateScreen(
    childId: Int,
    exerciseId: Int,
    onSessionStarted: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCreateData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Sesión") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = createState) {
                is CreateSessionUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CreateSessionUiState.Error -> {
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
                        Button(onClick = { viewModel.loadCreateData() }) {
                            Text("Reintentar")
                        }
                    }
                }

                is CreateSessionUiState.Ready -> {
                    val child = state.children.find { it.child_id == childId }
                    val exercise = state.exercises.find { it.id == exerciseId }
                    var selectedPencilId by remember { mutableStateOf<Int?>(null) }
                    var pencilDropdownExpanded by remember { mutableStateOf(false) }
                    val selectedPencil = state.pencils.find { it.pencil_id == selectedPencilId }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Confirmar Sesión",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Alumno",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = child?.name ?: "Desconocido",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Ejercicio",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = exercise?.name ?: "Desconocido",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = pencilDropdownExpanded,
                            onExpandedChange = { pencilDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedPencil?.let { it.name ?: it.device_uid } ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lápiz") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pencilDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                            )
                            ExposedDropdownMenu(
                                expanded = pencilDropdownExpanded,
                                onDismissRequest = { pencilDropdownExpanded = false }
                            ) {
                                if (state.pencils.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("No hay lápices disponibles") },
                                        onClick = { pencilDropdownExpanded = false }
                                    )
                                } else {
                                    state.pencils.forEach { pencil ->
                                        DropdownMenuItem(
                                            text = { Text(pencil.name ?: pencil.device_uid) },
                                            onClick = {
                                                selectedPencilId = pencil.pencil_id
                                                pencilDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                val pencilId = selectedPencilId
                                if (pencilId != null) {
                                    viewModel.createSession(childId, exerciseId, pencilId) { sessionId ->
                                        onSessionStarted(sessionId)
                                    }
                                }
                            },
                            enabled = selectedPencilId != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Iniciar Sesión")
                        }
                    }
                }
            }
        }
    }
}
