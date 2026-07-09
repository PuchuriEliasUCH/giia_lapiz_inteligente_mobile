package com.giia.lapiz_inteligente.models.session

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    @SerializedName("session_id")
    val session_id: Int,
    val child_id: Int,
    val exercise_id: Int,
    val pencil_id: Int? = null,
    val started_at: String,
    val ended_at: String?,
    val close_reason: String? = null,
    val avg_pressure: Double? = null,
    val max_pressure: Double? = null,
    val pressure_stability: Double? = null,
    val movement_stability: Double? = null,
    val tremor_level: Double? = null,
    val posture_score: Double? = null,
    val total_errors: Int? = null,
    val feedback_count: Int? = null,
    val ai_score: Double? = null,
    val result_summary: String? = null,
    val created_at: String
)
