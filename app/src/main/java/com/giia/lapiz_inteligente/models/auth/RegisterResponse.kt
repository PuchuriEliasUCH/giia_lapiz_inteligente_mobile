package com.giia.lapiz_inteligente.models.auth

data class RegisterResponse(
    val user_id: Int,
    val name: String,
    val email: String
)
