package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.exercise.CreateExerciseRequest
import com.giia.lapiz_inteligente.models.exercise.ExerciseDetailResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse
import com.giia.lapiz_inteligente.models.exercise.UpdateExerciseRequest
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStrokeTypes(): Result<List<StrokeTypeResponse>> {
        return try {
            Result.success(apiService.getStrokeTypes())
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getExercises(): Result<List<ExerciseResponse>> {
        return try {
            Result.success(apiService.getExercises())
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getExercisesByStrokeType(strokeTypeId: Int): Result<List<ExerciseResponse>> {
        return try {
            Result.success(apiService.getExercisesByStrokeType(strokeTypeId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getExerciseDetail(exerciseId: Int): Result<ExerciseDetailResponse> {
        return try {
            Result.success(apiService.getExerciseDetail(exerciseId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Ejercicio no encontrado."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun createExercise(request: CreateExerciseRequest): Result<ExerciseResponse> {
        return try {
            Result.success(apiService.createExercise(request))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Datos inválidos. Verifica los campos."))
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun updateExercise(exerciseId: Int, request: UpdateExerciseRequest): Result<ExerciseResponse> {
        return try {
            Result.success(apiService.updateExercise(exerciseId, request))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Datos inválidos."))
                404 -> Result.failure(Exception("Ejercicio no encontrado."))
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun deactivateExercise(exerciseId: Int): Result<ExerciseResponse> {
        return try {
            Result.success(apiService.deactivateExercise(exerciseId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("El ejercicio ya está desactivado."))
                404 -> Result.failure(Exception("Ejercicio no encontrado."))
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }
}
