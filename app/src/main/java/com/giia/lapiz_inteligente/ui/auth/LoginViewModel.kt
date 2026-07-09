package com.giia.lapiz_inteligente.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        val validationError = validateForm(email, password)
        if (validationError != null) {
            _uiState.value = AuthUiState.Error(validationError)
            return
        }

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success("Inicio de sesión exitoso") },
                onFailure = { AuthUiState.Error(parseError(it)) }
            )
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun parseError(error: Throwable): String {
        return error.message ?: "Error desconocido"
    }

    private fun validateForm(email: String, password: String): String? {
        return when {
            !isValidEmail(email) -> "Ingresa un correo electrónico válido."
            password.isBlank() -> "Ingresa tu contraseña."
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() &&
            email.contains("@") &&
            email.substringAfter("@").contains(".")
    }
}
