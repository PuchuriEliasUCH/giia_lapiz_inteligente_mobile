package com.giia.lapiz_inteligente.models.exercise

import com.google.gson.annotations.SerializedName

data class StrokeTypeResponse(
    @SerializedName("stroke_type_id")
    val id: Int,
    val name: String?,
    val description: String?
)
