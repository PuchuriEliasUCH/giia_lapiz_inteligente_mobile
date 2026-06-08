package com.giia.lapiz_inteligente.models.exercise

import com.google.gson.annotations.SerializedName

data class ExerciseDetailResponse(
    @SerializedName("exercise_id")
    val id: Int,
    val name: String?,
    val description: String?,
    @SerializedName("stroke_type")
    val stroke_type: String?,
    val instructions: String?
)
