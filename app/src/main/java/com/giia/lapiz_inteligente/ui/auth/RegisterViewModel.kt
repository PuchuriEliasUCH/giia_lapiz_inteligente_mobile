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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(
        name: String,
        lastname: String,
        email: String,
        password: String,
        phone: String? = null,
        confirmPassword: String = password,
        termsAccepted: Boolean = true
    ) {
        val validationError = validateForm(name, lastname, email, password, confirmPassword, termsAccepted)
        if (validationError != null) {
            _uiState.value = AuthUiState.Error(validationError)
            return
        }

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authRepository.register(name, lastname, email, password, phone)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success("Registro exitoso") },
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

    private fun validateForm(
        name: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String,
        termsAccepted: Boolean
    ): String? {
        return when {
            name.isBlank() -> "Ingresa tu nombre."
            lastname.isBlank() -> "Ingresa tu apellido."
            !isValidEmail(email) -> "Ingresa un correo electrónico válido."
            password.isBlank() -> "Ingresa una contraseña."
            password != confirmPassword -> "Las contraseñas no coinciden."
            !termsAccepted -> "Debes aceptar los términos y la política de privacidad."
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() &&
            email.contains("@") &&
            email.substringAfter("@").contains(".")
    }
}
