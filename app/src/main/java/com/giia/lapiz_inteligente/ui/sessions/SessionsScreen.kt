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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * Pantalla para crear una nueva sesión.
 *
 * Permite seleccionar un niño y un ejercicio, e iniciar la sesión.
 *
 * @param onSessionCreated Callback al crear la sesión, recibe el ID de la sesión.
 * @param viewModel ViewModel inyectado por Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onSessionCreated: (Int) -> Unit,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCreateData()
    }

    var selectedChildId by remember { mutableStateOf<Int?>(null) }
    var selectedExerciseId by remember { mutableStateOf<Int?>(null) }
    var selectedPencilId by remember { mutableStateOf<Int?>(null) }
    var childDropdownExpanded by remember { mutableStateOf(false) }
    var exerciseDropdownExpanded by remember { mutableStateOf(false) }
    var pencilDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Sesión") },
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
                        Button(onClick = { viewModel.loadCreateData() }) {
                            Text("Reintentar")
                        }
                    }
                }

                is CreateSessionUiState.Ready -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        val selectedChildName = state.children.find { it.child_id == selectedChildId }?.name ?: ""
                        val selectedExerciseName = state.exercises.find { it.id == selectedExerciseId }?.name ?: ""
                        val selectedPencilName = state.pencils.find { it.pencil_id == selectedPencilId }?.let { pencil ->
                            pencil.name ?: pencil.device_uid
                        } ?: ""

                        ExposedDropdownMenuBox(
                            expanded = childDropdownExpanded,
                            onExpandedChange = { childDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedChildName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar niño") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = childDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                            )
                            ExposedDropdownMenu(
                                expanded = childDropdownExpanded,
                                onDismissRequest = { childDropdownExpanded = false }
                            ) {
                                state.children.forEach { child ->
                                    DropdownMenuItem(
                                        text = { Text(child.name ?: "Sin nombre") },
                                        onClick = {
                                            selectedChildId = child.child_id
                                            childDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ExposedDropdownMenuBox(
                            expanded = exerciseDropdownExpanded,
                            onExpandedChange = { exerciseDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedExerciseName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar ejercicio") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = exerciseDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                            )
                            ExposedDropdownMenu(
                                expanded = exerciseDropdownExpanded,
                                onDismissRequest = { exerciseDropdownExpanded = false }
                            ) {
                                state.exercises.forEach { exercise ->
                                    DropdownMenuItem(
                                        text = { Text(exercise.name ?: "Sin nombre") },
                                        onClick = {
                                            selectedExerciseId = exercise.id
                                            exerciseDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ExposedDropdownMenuBox(
                            expanded = pencilDropdownExpanded,
                            onExpandedChange = { pencilDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedPencilName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar lápiz") },
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
                                val childId = selectedChildId
                                val exerciseId = selectedExerciseId
                                val pencilId = selectedPencilId
                                if (childId != null && exerciseId != null && pencilId != null) {
                                    viewModel.createSession(childId, exerciseId, pencilId) { sessionId ->
                                        onSessionCreated(sessionId)
                                    }
                                }
                            },
                            enabled = selectedChildId != null && selectedExerciseId != null && selectedPencilId != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Iniciar sesión")
                        }
                    }
                }
            }
        }
    }
}
