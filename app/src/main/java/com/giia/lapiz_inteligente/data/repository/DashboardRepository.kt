package com.giia.lapiz_inteligente.data.repository

import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.models.dashboard.DashboardSummary
import com.giia.lapiz_inteligente.models.session.SessionResponse
import java.io.IOException
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    private suspend fun getBearerToken(): String {
        val token = sessionManager.token.first()
        return "Bearer $token"
    }

    suspend fun getDashboardSummary(childId: Int, limit: Int = 10): Result<DashboardSummary> {
        return try {
            val token = getBearerToken()
            val sessions = apiService.getChildSessions(token, childId, limit = limit)
            if (sessions.isEmpty()) {
                return Result.success(DashboardSummary(
                    totalSessions = 0,
                    avgPressure = null,
                    maxPressure = null,
                    avgPressureStability = null,
                    avgMovementStability = null,
                    avgTremorLevel = null,
                    avgPostureScore = null,
                    performanceLevel = "Sin datos",
                    recommendation = "Realiza al menos una sesión para ver métricas."
                ))
            }
            val completed = sessions.filter { it.ended_at != null }
            val metrics = completed.ifEmpty { sessions }

            val avgPressure = metrics.mapNotNull { it.avg_pressure }.average().takeIf { !it.isNaN() }
            val maxPressure = metrics.mapNotNull { it.max_pressure }.maxOrNull()
            val avgPressureStability = metrics.mapNotNull { it.pressure_stability }.average().takeIf { !it.isNaN() }
            val avgMovementStability = metrics.mapNotNull { it.movement_stability }.average().takeIf { !it.isNaN() }
            val avgTremorLevel = metrics.mapNotNull { it.tremor_level }.average().takeIf { !it.isNaN() }
            val avgPostureScore = metrics.mapNotNull { it.posture_score }.average().takeIf { !it.isNaN() }

            val score = avgPostureScore ?: 0.0
            val level = when {
                score >= 80 -> "Excelente"
                score >= 60 -> "Bueno"
                score >= 40 -> "Regular"
                else -> "Mejorable"
            }
            val recommendation = when (level) {
                "Excelente" -> "El niño muestra un desempeño sobresaliente. Continúa con los ejercicios actuales."
                "Bueno" -> "Buen progreso. Se recomienda mantener la práctica constante."
                "Regular" -> "Se recomienda aumentar la frecuencia de los ejercicios y monitorear el progreso."
                else -> "Es recomendable consultar con un especialista para una evaluación más detallada."
            }

            Result.success(DashboardSummary(
                totalSessions = sessions.size,
                avgPressure = avgPressure,
                maxPressure = maxPressure,
                avgPressureStability = avgPressureStability,
                avgMovementStability = avgMovementStability,
                avgTremorLevel = avgTremorLevel,
                avgPostureScore = avgPostureScore,
                performanceLevel = level,
                recommendation = recommendation
            ))
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

    suspend fun getRecentSessions(childId: Int, limit: Int = 5): Result<List<SessionResponse>> {
        return try {
            val token = getBearerToken()
            Result.success(apiService.getChildSessions(token, childId, limit = limit))
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
}
