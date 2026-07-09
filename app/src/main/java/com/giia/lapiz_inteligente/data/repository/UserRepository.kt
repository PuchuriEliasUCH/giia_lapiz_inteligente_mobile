package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.auth.UserProfileResponse
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getProfile(): Result<UserProfileResponse> {
        return try {
            val response = apiService.getProfile()
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }
}
