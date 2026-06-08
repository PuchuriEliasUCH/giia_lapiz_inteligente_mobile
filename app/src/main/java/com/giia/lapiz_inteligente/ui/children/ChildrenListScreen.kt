package com.giia.lapiz_inteligente.ui.children

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.giia.lapiz_inteligente.models.child.ChildResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildrenListScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (ChildResponse) -> Unit,
    viewModel: ChildrenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var deactivateTarget by remember { mutableStateOf<ChildResponse?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Niños") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Agregar niño")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is ChildUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ChildUiState.Empty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No children registered yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateToAdd) {
                            Text(
                                text = "Agregar Niño",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                is ChildUiState.Error -> {
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
                        Button(onClick = { viewModel.loadChildren() }) {
                            Text(
                                text = "Reintentar",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                is ChildUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(state.children, key = { it.child_id }) { child ->
                            ChildCard(
                                child = child,
                                onEdit = { onNavigateToEdit(child) },
                                onDeactivate = { deactivateTarget = child }
                            )
                        }
                    }
                }
            }
        }
    }

    if (deactivateTarget != null) {
        val child = deactivateTarget!!
        AlertDialog(
            onDismissRequest = { deactivateTarget = null },
            title = { Text("Desactivar Niño") },
            text = { Text("¿Estás seguro de desactivar a ${child.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deactivateChild(child.child_id)
                        deactivateTarget = null
                    }
                ) {
                    Text("Confirmar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deactivateTarget = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
