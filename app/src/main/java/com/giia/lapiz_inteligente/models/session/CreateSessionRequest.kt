package com.giia.lapiz_inteligente.models.session

data class CreateSessionRequest(
    val child_id: Int,
    val exercise_id: Int
)
