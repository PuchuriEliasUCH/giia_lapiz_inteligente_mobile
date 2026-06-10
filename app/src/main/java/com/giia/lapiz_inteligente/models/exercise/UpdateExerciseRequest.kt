package com.giia.lapiz_inteligente.models.exercise

data class UpdateExerciseRequest(
    val name: String? = null,
    val description: String? = null,
    val stroke_type_id: Int? = null
)
