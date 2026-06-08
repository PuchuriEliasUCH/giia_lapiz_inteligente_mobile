package com.giia.lapiz_inteligente.models.exercise

import com.google.gson.annotations.SerializedName

data class ExerciseResponse(
    @SerializedName("exercise_id")
    val id: Int,
    val name: String?,
    val description: String?,
    @SerializedName("stroke_type_id")
    val stroke_type_id: Int
)
