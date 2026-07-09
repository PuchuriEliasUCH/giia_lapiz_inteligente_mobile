package com.giia.lapiz_inteligente.ui.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.repository.ChildRepository
import com.giia.lapiz_inteligente.data.repository.ExerciseRepository
import com.giia.lapiz_inteligente.domain.pencil.PencilUseCase
import com.giia.lapiz_inteligente.domain.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la gestión de sesiones.
 *
 * Administra el estado de creación, consulta e historial de sesiones,
 * delegando la lógica de negocio a [SessionUseCase].
 */
@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase,
    private val pencilUseCase: PencilUseCase,
    private val childRepository: ChildRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _createState = MutableStateFlow<CreateSessionUiState>(CreateSessionUiState.Loading)
    val createState: StateFlow<CreateSessionUiState> = _createState.asStateFlow()

    private val _activeSessionState = MutableStateFlow<ActiveSessionUiState?>(null)
    val activeSessionState: StateFlow<ActiveSessionUiState?> = _activeSessionState.asStateFlow()

    private val _historyState = MutableStateFlow<SessionHistoryUiState>(SessionHistoryUiState.Loading)
    val historyState: StateFlow<SessionHistoryUiState> = _historyState.asStateFlow()

    private var activeSessionId: Int? = null

    private val _children = MutableStateFlow<List<com.giia.lapiz_inteligente.models.child.ChildResponse>>(emptyList())
    val children: StateFlow<List<com.giia.lapiz_inteligente.models.child.ChildResponse>> = _children.asStateFlow()

    /**
     * Carga los datos necesarios para crear una sesión (niños y ejercicios).
     */
    fun loadCreateData() {
        _createState.value = CreateSessionUiState.Loading
        viewModelScope.launch {
            val childrenResult = childRepository.getChildren()
            val exercisesResult = exerciseRepository.getExercises()
            val pencilsResult = pencilUseCase.getPencils(status = "available")

            val children = childrenResult.getOrNull().orEmpty()
            val exercises = exercisesResult.getOrNull().orEmpty()
            val pencils = pencilsResult.getOrNull().orEmpty()

            _children.value = children.filter { it.is_active }

            _createState.value = CreateSessionUiState.Ready(
                children = children.filter { it.is_active },
                exercises = exercises,
                pencils = pencils.filter { it.status == "available" }
            )
        }
    }

    /**
     * Carga la lista de niños activos para el selector de historial.
     */
    fun loadChildren() {
        viewModelScope.launch {
            val result = childRepository.getChildren()
            _children.value = result.getOrNull().orEmpty().filter { it.is_active }
        }
    }

    /**
     * Crea una nueva sesión para el niño y ejercicio especificados.
     *
     * @param childId Identificador del niño.
     * @param exerciseId Identificador del ejercicio.
     * @param pencilId Identificador del lápiz.
     * @param onSuccess Callback ejecutado al crear la sesión exitosamente.
     */
    fun createSession(childId: Int, exerciseId: Int, pencilId: Int, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val result = sessionUseCase.createSession(childId, exerciseId, pencilId)
            result.fold(
                onSuccess = { session ->
                    activeSessionId = session.session_id
                    _activeSessionState.value = ActiveSessionUiState.Success(session)
                    onSuccess(session.session_id)
                },
                onFailure = {
                    _createState.value = CreateSessionUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    /**
     * Carga el detalle de una sesión activa por su identificador.
     *
     * @param sessionId Identificador de la sesión.
     */
    fun loadActiveSession(sessionId: Int) {
        activeSessionId = sessionId
        _activeSessionState.value = ActiveSessionUiState.Loading
        viewModelScope.launch {
            val result = sessionUseCase.getSession(sessionId)
            _activeSessionState.value = result.fold(
                onSuccess = { ActiveSessionUiState.Success(it) },
                onFailure = { ActiveSessionUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    /**
     * Finaliza la sesión activa.
     *
     * @param onSuccess Callback ejecutado al finalizar la sesión.
     */
    fun endSession(onSuccess: () -> Unit) {
        val sessionId = activeSessionId ?: return
        viewModelScope.launch {
            val result = sessionUseCase.endSession(sessionId)
            result.fold(
                onSuccess = {
                    _activeSessionState.value = null
                    activeSessionId = null
                    onSuccess()
                },
                onFailure = {
                    _activeSessionState.value = ActiveSessionUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    /**
     * Carga el historial de sesiones de un niño.
     *
     * @param childId Identificador del niño.
     */
    fun loadHistory(childId: Int) {
        _historyState.value = SessionHistoryUiState.Loading
        viewModelScope.launch {
            val result = sessionUseCase.getChildSessions(childId)
            _historyState.value = result.fold(
                onSuccess = { sessions ->
                    if (sessions.isEmpty()) SessionHistoryUiState.Empty
                    else SessionHistoryUiState.Success(sessions)
                },
                onFailure = { SessionHistoryUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }
}
