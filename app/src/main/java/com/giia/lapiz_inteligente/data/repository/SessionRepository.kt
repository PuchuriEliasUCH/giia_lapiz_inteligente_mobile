package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.session.CreateSessionRequest
import com.giia.lapiz_inteligente.models.session.EndSessionRequest
import com.giia.lapiz_inteligente.models.session.SessionResponse
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createSession(childId: Int, exerciseId: Int, pencilId: Int): Result<SessionResponse> {
        return try {
            Result.success(
                apiService.createSession(CreateSessionRequest(childId, exerciseId, pencilId))
            )
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Niño, ejercicio o lápiz no encontrado."))
                409 -> Result.failure(Exception("El lápiz ya está en uso o ya existe una sesión activa."))
                422 -> Result.failure(Exception("Datos inválidos. Verifica la sesión."))
                423 -> Result.failure(Exception("El lápiz no está disponible."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getActiveSessionByChild(childId: Int): Result<SessionResponse?> {
        return getActiveSession(childId = childId, pencilId = null)
    }

    suspend fun getActiveSessionByPencil(pencilId: Int): Result<SessionResponse?> {
        return getActiveSession(childId = null, pencilId = pencilId)
    }

    private suspend fun getActiveSession(childId: Int?, pencilId: Int?): Result<SessionResponse?> {
        return try {
            Result.success(apiService.getActiveSession(childId = childId, pencilId = pencilId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Debes enviar un niño o un lápiz para consultar sesión activa."))
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.success(null)
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getSession(sessionId: Int): Result<SessionResponse> {
        return try {
            Result.success(apiService.getSession(sessionId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Sesión no encontrada."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getChildSessions(childId: Int): Result<List<SessionResponse>> {
        return try {
            Result.success(apiService.getChildSessions(childId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Niño no encontrado."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun endSession(sessionId: Int, closeReason: String = "manual"): Result<SessionResponse> {
        return try {
            Result.success(
                apiService.endSession(sessionId, EndSessionRequest(closeReason))
            )
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Sesión no encontrada."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }
}
