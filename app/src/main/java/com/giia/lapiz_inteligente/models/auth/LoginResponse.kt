package com.giia.lapiz_inteligente.models.auth

data class LoginResponse(
    val access_token: String,
    val token_type: String
)
