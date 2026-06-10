package com.giia.lapiz_inteligente.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.domain.exercise.ExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de ejercicios.
 *
 * Administra el estado de la lista de ejercicios y el detalle individual,
 * delegando la lógica de negocio a [ExerciseUseCase].
 */
@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exerciseUseCase: ExerciseUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow<ExerciseListUiState>(ExerciseListUiState.Loading)
    val listState: StateFlow<ExerciseListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<ExerciseDetailUiState?>(null)
    val detailState: StateFlow<ExerciseDetailUiState?> = _detailState.asStateFlow()

    init {
        loadExercises()
    }

    /**
     * Carga la lista de ejercicios junto con los tipos de trazo.
     */
    fun loadExercises() {
        _listState.value = ExerciseListUiState.Loading
        viewModelScope.launch {
            val exercisesResult = exerciseUseCase.getExercises()
            val strokeTypesResult = exerciseUseCase.getStrokeTypes()

            val exercises = exercisesResult.getOrNull().orEmpty()
            val strokeTypes = strokeTypesResult.getOrNull().orEmpty()

            _listState.value = if (exercises.isEmpty()) {
                ExerciseListUiState.Empty
            } else {
                ExerciseListUiState.Success(
                    exercises = exercises,
                    strokeTypes = strokeTypes,
                    selectedStrokeTypeId = null
                )
            }
        }
    }

    /**
     * Filtra los ejercicios por tipo de trazo.
     *
     * @param strokeTypeId Identificador del tipo de trazo, o null para mostrar todos.
     */
    fun filterByStrokeType(strokeTypeId: Int?) {
        if (strokeTypeId == null) {
            loadExercises()
            return
        }
        _listState.value = ExerciseListUiState.Loading
        viewModelScope.launch {
            val result = exerciseUseCase.getExercisesByStrokeType(strokeTypeId)
            val strokeTypesResult = exerciseUseCase.getStrokeTypes()
            val strokeTypes = strokeTypesResult.getOrNull().orEmpty()

            result.fold(
                onSuccess = { exercises ->
                    _listState.value = if (exercises.isEmpty()) ExerciseListUiState.Empty
                    else ExerciseListUiState.Success(
                        exercises = exercises,
                        strokeTypes = strokeTypes,
                        selectedStrokeTypeId = strokeTypeId
                    )
                },
                onFailure = {
                    _listState.value = ExerciseListUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    /**
     * Carga el detalle de un ejercicio por su identificador.
     *
     * @param exerciseId Identificador del ejercicio.
     */
    fun loadExerciseDetail(exerciseId: Int) {
        _detailState.value = ExerciseDetailUiState.Loading
        viewModelScope.launch {
            val result = exerciseUseCase.getExerciseDetail(exerciseId)
            _detailState.value = result.fold(
                onSuccess = { ExerciseDetailUiState.Success(it) },
                onFailure = { ExerciseDetailUiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    /**
     * Limpia el estado del detalle para liberar recursos.
     */
    fun clearDetail() {
        _detailState.value = null
    }

    fun createExercise(
        name: String,
        description: String?,
        strokeTypeId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = exerciseUseCase.createExercise(name, description, strokeTypeId)
            result.fold(
                onSuccess = {
                    loadExercises()
                    onSuccess()
                },
                onFailure = {
                    onError(it.message ?: "Error al crear ejercicio")
                }
            )
        }
    }

    fun updateExercise(
        exerciseId: Int,
        name: String?,
        description: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = exerciseUseCase.updateExercise(exerciseId, name, description)
            result.fold(
                onSuccess = {
                    loadExercises()
                    onSuccess()
                },
                onFailure = {
                    onError(it.message ?: "Error al actualizar ejercicio")
                }
            )
        }
    }

    fun deactivateExercise(
        exerciseId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = exerciseUseCase.deactivateExercise(exerciseId)
            result.fold(
                onSuccess = {
                    loadExercises()
                    onSuccess()
                },
                onFailure = {
                    onError(it.message ?: "Error al desactivar ejercicio")
                }
            )
        }
    }
}
