package com.giia.lapiz_inteligente.ui.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse

/**
 * Sección de filtro por tipo de trazo usando un dropdown.
 *
 * @param strokeTypes Lista de tipos de trazo disponibles.
 * @param selectedTypeId Identificador del tipo seleccionado, o null para "Todos".
 * @param onTypeSelected Callback al seleccionar un tipo (null para limpiar filtro).
 * @param modifier Modificador opcional para personalizar el layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    strokeTypes: List<StrokeTypeResponse>,
    selectedTypeId: Int?,
    onTypeSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTypeName = strokeTypes.find { it.id == selectedTypeId }?.name ?: "Todos"
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = selectedTypeName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Filtrar por tipo de trazo") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onTypeSelected(null)
                    expanded = false
                }
            )
            strokeTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name ?: "Sin nombre") },
                    onClick = {
                        onTypeSelected(type.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
