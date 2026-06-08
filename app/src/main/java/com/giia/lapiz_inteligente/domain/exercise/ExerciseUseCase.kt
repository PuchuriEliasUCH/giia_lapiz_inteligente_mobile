package com.giia.lapiz_inteligente.domain.exercise

import com.giia.lapiz_inteligente.data.repository.ExerciseRepository
import com.giia.lapiz_inteligente.models.exercise.ExerciseDetailResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse
import javax.inject.Inject

/**
 * Caso de uso para la gestión de ejercicios.
 *
 * Orquesta las operaciones del catálogo de ejercicios y tipos de trazo,
 * actuando como intermediario entre la capa de presentación y el repositorio.
 */
class ExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {

    /**
     * Obtiene todos los tipos de trazo disponibles.
     *
     * @return Result con la lista de [StrokeTypeResponse] o un error.
     */
    suspend fun getStrokeTypes(): Result<List<StrokeTypeResponse>> {
        return exerciseRepository.getStrokeTypes()
    }

    /**
     * Obtiene todos los ejercicios del catálogo.
     *
     * @return Result con la lista de [ExerciseResponse] o un error.
     */
    suspend fun getExercises(): Result<List<ExerciseResponse>> {
        return exerciseRepository.getExercises()
    }

    /**
     * Obtiene ejercicios filtrados por tipo de trazo.
     *
     * @param strokeTypeId Identificador del tipo de trazo.
     * @return Result con la lista de [ExerciseResponse] filtrada o un error.
     */
    suspend fun getExercisesByStrokeType(strokeTypeId: Int): Result<List<ExerciseResponse>> {
        return exerciseRepository.getExercisesByStrokeType(strokeTypeId)
    }

    /**
     * Obtiene el detalle completo de un ejercicio.
     *
     * @param exerciseId Identificador del ejercicio.
     * @return Result con [ExerciseDetailResponse] o un error.
     */
    suspend fun getExerciseDetail(exerciseId: Int): Result<ExerciseDetailResponse> {
        return exerciseRepository.getExerciseDetail(exerciseId)
    }
}
