package com.giia.lapiz_inteligente.ui.sessions

import com.giia.lapiz_inteligente.models.child.ChildResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.pencil.PencilResponse
import com.giia.lapiz_inteligente.models.session.SessionResponse

/**
 * Estados posibles para la pantalla de creación de sesión.
 */
sealed interface CreateSessionUiState {
    /** Cargando datos necesarios (niños, ejercicios). */
    data object Loading : CreateSessionUiState
    /** Datos cargados listos para crear sesión. */
    data class Ready(
        val children: List<ChildResponse>,
        val exercises: List<ExerciseResponse>,
        val pencils: List<PencilResponse>
    ) : CreateSessionUiState
    /** Ocurrió un error al cargar los datos. */
    data class Error(val message: String) : CreateSessionUiState
}

/**
 * Estados posibles para la pantalla de detalle de sesión activa.
 */
sealed interface ActiveSessionUiState {
    /** Cargando detalle de la sesión. */
    data object Loading : ActiveSessionUiState
    /** Sesión activa cargada correctamente. */
    data class Success(val session: SessionResponse) : ActiveSessionUiState
    /** Ocurrió un error al cargar la sesión. */
    data class Error(val message: String) : ActiveSessionUiState
}

/**
 * Estados posibles para la pantalla de historial de sesiones.
 */
sealed interface SessionHistoryUiState {
    /** Cargando historial. */
    data object Loading : SessionHistoryUiState
    /** Historial cargado correctamente. */
    data class Success(val sessions: List<SessionResponse>) : SessionHistoryUiState
    /** No hay sesiones registradas. */
    data object Empty : SessionHistoryUiState
    /** Ocurrió un error al cargar el historial. */
    data class Error(val message: String) : SessionHistoryUiState
}
