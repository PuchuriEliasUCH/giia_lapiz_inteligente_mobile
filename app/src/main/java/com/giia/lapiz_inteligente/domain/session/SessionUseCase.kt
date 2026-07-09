package com.giia.lapiz_inteligente.domain.session

import com.giia.lapiz_inteligente.data.repository.SessionRepository
import com.giia.lapiz_inteligente.models.session.SessionResponse
import javax.inject.Inject

/**
 * Caso de uso para la gestión de sesiones de escritura.
 *
 * Orquesta las operaciones de creación, consulta, finalización e historial
 * de sesiones, actuando como intermediario entre la capa de presentación
 * y el repositorio.
 */
class SessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {

    suspend fun createSession(childId: Int, exerciseId: Int, pencilId: Int): Result<SessionResponse> {
        return sessionRepository.createSession(childId, exerciseId, pencilId)
    }

    suspend fun getActiveSessionByChild(childId: Int): Result<SessionResponse?> {
        return sessionRepository.getActiveSessionByChild(childId)
    }

    suspend fun getActiveSessionByPencil(pencilId: Int): Result<SessionResponse?> {
        return sessionRepository.getActiveSessionByPencil(pencilId)
    }

    suspend fun getSession(sessionId: Int): Result<SessionResponse> {
        return sessionRepository.getSession(sessionId)
    }

    suspend fun getChildSessions(childId: Int): Result<List<SessionResponse>> {
        return sessionRepository.getChildSessions(childId)
    }

    suspend fun endSession(sessionId: Int, closeReason: String = "manual"): Result<SessionResponse> {
        return sessionRepository.endSession(sessionId, closeReason)
    }
}
