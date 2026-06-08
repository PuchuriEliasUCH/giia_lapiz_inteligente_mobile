package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.exercise.ExerciseDetailResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse
import java.io.IOException
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    private suspend fun getBearerToken(): String {
        val token = sessionManager.token.first()
        return "Bearer $token"
    }

    suspend fun getStrokeTypes(): Result<List<StrokeTypeResponse>> {
        return try {
            val token = getBearerToken()
            Result.success(apiService.getStrokeTypes(token))
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
            val token = getBearerToken()
            Result.success(apiService.getExercises(token))
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
            val token = getBearerToken()
            Result.success(apiService.getExercisesByStrokeType(token, strokeTypeId))
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
            val token = getBearerToken()
            Result.success(apiService.getExerciseDetail(token, exerciseId))
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
}
