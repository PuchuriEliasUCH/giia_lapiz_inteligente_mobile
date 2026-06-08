package com.giia.lapiz_inteligente.models.session

import com.google.gson.annotations.SerializedName

data class SessionHistoryResponse(
    @SerializedName("session_id")
    val session_id: Int,
    val exercise_name: String?,
    val started_at: String,
    val ended_at: String?
)
