package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.auth.LoginRequest
import com.giia.lapiz_inteligente.models.auth.LoginResponse
import com.giia.lapiz_inteligente.models.auth.RegisterRequest
import com.giia.lapiz_inteligente.models.auth.RegisterResponse
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            sessionManager.saveToken(response.access_token)
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Credenciales inválidas."))
                400 -> Result.failure(Exception("Solicitud inválida. Verifica tus datos."))
                500 -> Result.failure(Exception("Error interno del servidor. Intenta más tarde."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun register(
        name: String,
        lastname: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, lastname, email, password))
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> Result.failure(Exception("El email ya está registrado."))
                400 -> Result.failure(Exception("Solicitud inválida. Verifica tus datos."))
                500 -> Result.failure(Exception("Error interno del servidor. Intenta más tarde."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun logout() {
        sessionManager.clearToken()
    }
}
