package com.giia.lapiz_inteligente.models.child

data class ChildResponse(
    val child_id: Int,
    val user_id: Int,
    val name: String,
    val birth_date: String?,
    val dominant_hand: String?,
    val school_grade: String?,
    val notes: String?,
    val is_active: Boolean,
    val created_at: String
)
