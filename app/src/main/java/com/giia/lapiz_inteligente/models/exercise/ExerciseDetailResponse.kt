package com.giia.lapiz_inteligente.models.exercise

import com.google.gson.annotations.SerializedName

data class ExerciseDetailResponse(
    @SerializedName("exercise_id")
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("stroke_type_id")
    val stroke_type_id: Int,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String
)
