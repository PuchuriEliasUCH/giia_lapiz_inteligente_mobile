package com.giia.lapiz_inteligente.models.auth

data class UserProfileResponse(
    val user_id: Int,
    val name: String,
    val lastname: String,
    val email: String,
    val phone: String?,
    val is_active: Boolean,
    val created_at: String
)
