package com.giia.lapiz_inteligente.models.child

data class UpdateChildRequest(
    val name: String? = null,
    val birth_date: String? = null,
    val dominant_hand: String? = null,
    val school_grade: String? = null,
    val notes: String? = null
)
