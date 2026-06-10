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
        phone: String? = null
    ) {
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
}
