package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.session.CreateSessionRequest
import com.giia.lapiz_inteligente.models.session.EndSessionRequest
import com.giia.lapiz_inteligente.models.session.SessionResponse
import java.io.IOException
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    private suspend fun getBearerToken(): String {
        val token = sessionManager.token.first()
        return "Bearer $token"
    }

    suspend fun createSession(childId: Int, exerciseId: Int): Result<SessionResponse> {
        return try {
            val token = getBearerToken()
            Result.success(
                apiService.createSession(token, CreateSessionRequest(childId, exerciseId))
            )
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión nuevamente."))
                404 -> Result.failure(Exception("Niño o ejercicio no encontrado."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun getSession(sessionId: Int): Result<SessionResponse> {
        return try {
            val token = getBearerToken()
            Result.success(apiService.getSession(token, sessionId))
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
            val token = getBearerToken()
            Result.success(apiService.getChildSessions(token, childId))
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
            val token = getBearerToken()
            Result.success(
                apiService.endSession(token, sessionId, EndSessionRequest(closeReason))
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
