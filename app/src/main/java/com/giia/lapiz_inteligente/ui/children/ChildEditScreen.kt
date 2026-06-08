package com.giia.lapiz_inteligente.ui.children

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildEditScreen(
    childId: Int,
    currentName: String,
    onNavigateBack: () -> Unit,
    viewModel: ChildrenViewModel = hiltViewModel()
) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val dominantHandOptions = listOf("derecha", "izquierda", "ambidiestro")

    val existingChild = remember(viewModel.uiState.value, childId) {
        val state = viewModel.uiState.value
        if (state is ChildUiState.Success) {
            state.children.find { it.child_id == childId }
        } else null
    }

    var name by remember { mutableStateOf(currentName) }
    var birthDateMillis by remember { mutableStateOf<Long?>(null) }
    var birthDateText by remember { mutableStateOf(existingChild?.birth_date ?: "") }
    var dominantHand by remember { mutableStateOf(existingChild?.dominant_hand ?: "") }
    var schoolGrade by remember { mutableStateOf(existingChild?.school_grade ?: "") }
    var notes by remember { mutableStateOf(existingChild?.notes ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(existingChild) {
        if (existingChild != null && !initialized) {
            birthDateText = existingChild.birth_date ?: ""
            dominantHand = existingChild.dominant_hand ?: ""
            schoolGrade = existingChild.school_grade ?: ""
            notes = existingChild.notes ?: ""
            initialized = true
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = birthDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    birthDateMillis = datePickerState.selectedDateMillis
                    birthDateText = birthDateMillis?.let { dateFormat.format(Date(it)) } ?: ""
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Niño") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = { Text("Nombre del Niño *") },
                singleLine = true,
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = birthDateText,
                onValueChange = {},
                label = { Text("Fecha de Nacimiento") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Seleccionar")
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = dominantHand,
                    onValueChange = {},
                    label = { Text("Mano Dominante") },
                    readOnly = true,
                    singleLine = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    dominantHandOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                dominantHand = option
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = schoolGrade,
                onValueChange = { schoolGrade = it },
                    label = { Text("Grado Escolar") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                    label = { Text("Observaciones") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        name.isBlank() ->                         nameError = "El nombre no puede estar vacío"
                        name.length < 2 ->                         nameError = "El nombre debe tener al menos 2 caracteres"
                        else -> {
                            nameError = null
                            viewModel.updateChild(
                                childId = childId,
                                name = name.trim(),
                                birthDate = birthDateText.ifBlank { null },
                                dominantHand = dominantHand.ifBlank { null },
                                schoolGrade = schoolGrade.ifBlank { null },
                                notes = notes.ifBlank { null },
                                onSuccess = { onNavigateBack() }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Actualizar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
