package com.giia.lapiz_inteligente.models.auth

data class RegisterRequest(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String? = null
)
