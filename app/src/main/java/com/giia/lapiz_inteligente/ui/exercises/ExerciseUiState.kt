package com.giia.lapiz_inteligente.ui.exercises

import com.giia.lapiz_inteligente.models.exercise.ExerciseDetailResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse

/**
 * Estados posibles para la pantalla de lista de ejercicios.
 */
sealed interface ExerciseListUiState {
    /** Cargando datos desde el backend. */
    data object Loading : ExerciseListUiState

    /** Datos cargados correctamente. */
    data class Success(
        val exercises: List<ExerciseResponse>,
        val strokeTypes: List<StrokeTypeResponse>,
        val selectedStrokeTypeId: Int? = null
    ) : ExerciseListUiState

    /** No hay ejercicios disponibles. */
    data object Empty : ExerciseListUiState

    /** Ocurrió un error al cargar los datos. */
    data class Error(val message: String) : ExerciseListUiState
}

/**
 * Estados posibles para la pantalla de detalle de un ejercicio.
 */
sealed interface ExerciseDetailUiState {
    /** Cargando detalle desde el backend. */
    data object Loading : ExerciseDetailUiState

    /** Detalle cargado correctamente. */
    data class Success(val detail: ExerciseDetailResponse) : ExerciseDetailUiState

    /** Ocurrió un error al cargar el detalle. */
    data class Error(val message: String) : ExerciseDetailUiState
}
