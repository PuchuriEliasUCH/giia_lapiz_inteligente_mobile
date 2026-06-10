package com.giia.lapiz_inteligente.models.exercise

data class CreateExerciseRequest(
    val name: String,
    val description: String? = null,
    val stroke_type_id: Int
)
