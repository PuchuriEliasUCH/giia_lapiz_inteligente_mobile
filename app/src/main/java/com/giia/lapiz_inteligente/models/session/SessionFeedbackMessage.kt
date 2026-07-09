package com.giia.lapiz_inteligente.models.session

data class SessionFeedbackMessage(
    val type: AlertType,
    val severity: FeedbackSeverity,
    val message: String,
    val timestamp: String,
    val session_id: Int,
    val value: Double? = null
)
