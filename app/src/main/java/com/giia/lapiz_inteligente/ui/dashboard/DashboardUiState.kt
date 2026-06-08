package com.giia.lapiz_inteligente.ui.dashboard

import com.giia.lapiz_inteligente.models.dashboard.DashboardSummary
import com.giia.lapiz_inteligente.models.session.SessionResponse

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val summary: DashboardSummary,
        val recentSessions: List<SessionResponse>
    ) : DashboardUiState
    data object Empty : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
