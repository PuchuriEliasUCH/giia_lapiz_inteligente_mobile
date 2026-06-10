package com.giia.lapiz_inteligente.ui.children

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.repository.ChildRepository
import com.giia.lapiz_inteligente.data.repository.DashboardRepository
import com.giia.lapiz_inteligente.data.repository.SessionRepository
import com.giia.lapiz_inteligente.models.child.ChildResponse
import com.giia.lapiz_inteligente.models.dashboard.DashboardSummary
import com.giia.lapiz_inteligente.models.session.SessionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ChildDetailState {
    data object Loading : ChildDetailState
    data class Success(
        val child: ChildResponse,
        val sessions: List<SessionResponse>,
        val metrics: DashboardSummary
    ) : ChildDetailState
    data class Error(val message: String) : ChildDetailState
}

@HiltViewModel
class ChildDetailViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val sessionRepository: SessionRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ChildDetailState>(ChildDetailState.Loading)
    val state: StateFlow<ChildDetailState> = _state.asStateFlow()

    fun loadChildDetail(childId: Int) {
        _state.value = ChildDetailState.Loading
        viewModelScope.launch {
            val childResult = childRepository.getChild(childId)
            val sessionsResult = sessionRepository.getChildSessions(childId)
            val metricsResult = dashboardRepository.getDashboardSummary(childId)

            val child = childResult.getOrNull()
            val sessions = sessionsResult.getOrNull().orEmpty()
            val metrics = metricsResult.getOrNull()

            if (child != null) {
                _state.value = ChildDetailState.Success(
                    child = child,
                    sessions = sessions,
                    metrics = metrics ?: DashboardSummary(
                        totalSessions = sessions.size,
                        avgPressure = null,
                        maxPressure = null,
                        avgPressureStability = null,
                        avgMovementStability = null,
                        avgTremorLevel = null,
                        avgPostureScore = null,
                        performanceLevel = "Sin datos",
                        recommendation = null
                    )
                )
            } else {
                val error = childResult.exceptionOrNull()
                _state.value = ChildDetailState.Error(
                    error?.message ?: "Error al cargar datos del niño"
                )
            }
        }
    }
}
