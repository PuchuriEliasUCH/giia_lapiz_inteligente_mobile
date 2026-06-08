package com.giia.lapiz_inteligente.ui.children

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.repository.ChildRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildrenViewModel @Inject constructor(
    private val childRepository: ChildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChildUiState>(ChildUiState.Loading)
    val uiState: StateFlow<ChildUiState> = _uiState.asStateFlow()

    init {
        loadChildren()
    }

    fun loadChildren() {
        _uiState.value = ChildUiState.Loading
        viewModelScope.launch {
            val result = childRepository.getChildren()
            _uiState.value = result.fold(
                onSuccess = { children ->
                    if (children.isEmpty()) ChildUiState.Empty
                    else ChildUiState.Success(children)
                },
                onFailure = { ChildUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun createChild(
        name: String,
        birthDate: String? = null,
        dominantHand: String? = null,
        schoolGrade: String? = null,
        notes: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = childRepository.createChild(
                name = name,
                birthDate = birthDate,
                dominantHand = dominantHand,
                schoolGrade = schoolGrade,
                notes = notes
            )
            result.fold(
                onSuccess = {
                    loadChildren()
                    onSuccess()
                },
                onFailure = {
                    _uiState.value = ChildUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    fun updateChild(
        childId: Int,
        name: String,
        birthDate: String? = null,
        dominantHand: String? = null,
        schoolGrade: String? = null,
        notes: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = childRepository.updateChild(
                childId = childId,
                name = name,
                birthDate = birthDate,
                dominantHand = dominantHand,
                schoolGrade = schoolGrade,
                notes = notes
            )
            result.fold(
                onSuccess = {
                    loadChildren()
                    onSuccess()
                },
                onFailure = {
                    _uiState.value = ChildUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    fun deactivateChild(id: Int) {
        viewModelScope.launch {
            val result = childRepository.deactivateChild(id)
            result.fold(
                onSuccess = { loadChildren() },
                onFailure = {
                    _uiState.value = ChildUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }
}
