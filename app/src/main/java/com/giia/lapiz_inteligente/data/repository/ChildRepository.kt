package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.child.ChildResponse
import com.giia.lapiz_inteligente.models.child.CreateChildRequest
import com.giia.lapiz_inteligente.models.child.UpdateChildRequest
import java.io.IOException
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class ChildRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    private suspend fun getBearerToken(): String {
        val token = sessionManager.token.first()
        return "Bearer $token"
    }

    suspend fun getChildren(): Result<List<ChildResponse>> {
        return try {
            val token = getBearerToken()
            Result.success(apiService.getChildren(token))
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

    suspend fun createChild(
        name: String,
        birthDate: String? = null,
        dominantHand: String? = null,
        schoolGrade: String? = null,
        notes: String? = null
    ): Result<ChildResponse> {
        return try {
            val token = getBearerToken()
            Result.success(
                apiService.createChild(
                    token,
                    CreateChildRequest(
                        name = name,
                        birth_date = birthDate,
                        dominant_hand = dominantHand,
                        school_grade = schoolGrade,
                        notes = notes
                    )
                )
            )
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Nombre inválido."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun updateChild(
        childId: Int,
        name: String,
        birthDate: String? = null,
        dominantHand: String? = null,
        schoolGrade: String? = null,
        notes: String? = null
    ): Result<ChildResponse> {
        return try {
            val token = getBearerToken()
            Result.success(
                apiService.updateChild(
                    token, childId,
                    UpdateChildRequest(
                        name = name,
                        birth_date = birthDate,
                        dominant_hand = dominantHand,
                        school_grade = schoolGrade,
                        notes = notes
                    )
                )
            )
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Nombre inválido."))
                404 -> Result.failure(Exception("Niño no encontrado."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }

    suspend fun deactivateChild(childId: Int): Result<ChildResponse> {
        return try {
            val token = getBearerToken()
            Result.success(apiService.deactivateChild(token, childId))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Result.failure(Exception("Niño no encontrado."))
                else -> Result.failure(Exception("Error del servidor (${e.code()})."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido: ${e.message}"))
        }
    }
}
