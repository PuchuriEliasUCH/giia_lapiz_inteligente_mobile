package com.giia.lapiz_inteligente.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.repository.ChildRepository
import com.giia.lapiz_inteligente.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val childRepository: ChildRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    fun loadDashboard(childId: Int? = null) {
        _state.value = DashboardUiState.Loading
        viewModelScope.launch {
            val targetChildId = if (childId != null) {
                childId
            } else {
                val childrenResult = childRepository.getChildren()
                val activeChildren = childrenResult.getOrNull().orEmpty().filter { it.is_active }
                if (activeChildren.isEmpty()) {
                    _state.value = DashboardUiState.Empty
                    return@launch
                }
                activeChildren.first().child_id
            }

            val summaryResult = dashboardRepository.getDashboardSummary(targetChildId)
            val sessionsResult = dashboardRepository.getRecentSessions(targetChildId)

            summaryResult.fold(
                onSuccess = { summary ->
                    val sessions = sessionsResult.getOrNull().orEmpty()
                    if (summary.totalSessions == 0) {
                        _state.value = DashboardUiState.Empty
                    } else {
                        _state.value = DashboardUiState.Success(summary, sessions)
                    }
                },
                onFailure = {
                    _state.value = DashboardUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }
}
