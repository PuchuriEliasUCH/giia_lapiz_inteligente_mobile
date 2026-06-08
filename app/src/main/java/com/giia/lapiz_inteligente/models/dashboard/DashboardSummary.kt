package com.giia.lapiz_inteligente.models.dashboard

data class DashboardSummary(
    val totalSessions: Int,
    val avgPressure: Double?,
    val maxPressure: Double?,
    val avgPressureStability: Double?,
    val avgMovementStability: Double?,
    val avgTremorLevel: Double?,
    val avgPostureScore: Double?,
    val performanceLevel: String,
    val recommendation: String?
)
