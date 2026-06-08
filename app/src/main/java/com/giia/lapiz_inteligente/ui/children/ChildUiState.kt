package com.giia.lapiz_inteligente.ui.children

import com.giia.lapiz_inteligente.models.child.ChildResponse

sealed interface ChildUiState {
    data object Loading : ChildUiState
    data class Success(val children: List<ChildResponse>) : ChildUiState
    data object Empty : ChildUiState
    data class Error(val message: String) : ChildUiState
}
