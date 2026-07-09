package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.pencil.CreatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.PencilResponse
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilStatusRequest
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PencilRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPencils(status: String? = null): Result<List<PencilResponse>> {
        return try {
            Result.success(apiService.getPencils(status))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Result.failure(mapHttpError(e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getPencil(pencilId: Int): Result<PencilResponse> {
        return try {
            Result.success(apiService.getPencil(pencilId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Result.failure(mapHttpError(e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun createPencil(request: CreatePencilRequest): Result<PencilResponse> {
        return try {
            Result.success(apiService.createPencil(request))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Result.failure(mapHttpError(e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun updatePencil(pencilId: Int, request: UpdatePencilRequest): Result<PencilResponse> {
        return try {
            Result.success(apiService.updatePencil(pencilId, request))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Result.failure(mapHttpError(e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun updatePencilStatus(
        pencilId: Int,
        request: UpdatePencilStatusRequest
    ): Result<PencilResponse> {
        return try {
            Result.success(apiService.updatePencilStatus(pencilId, request))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Result.failure(mapHttpError(e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    private fun mapHttpError(error: HttpException): Exception {
        return when (error.code()) {
            400 -> Exception("Datos inválidos o identificador de dispositivo duplicado.")
            401 -> Exception("Sesión expirada. Inicia sesión nuevamente.")
            404 -> Exception("Lápiz no encontrado.")
            409 -> Exception("Conflicto de estado del lápiz.")
            422 -> Exception("Datos inválidos. Verifica los campos.")
            423 -> Exception("El lápiz no está disponible.")
            else -> Exception("Error del servidor (${error.code()}).")
        }
    }
}
