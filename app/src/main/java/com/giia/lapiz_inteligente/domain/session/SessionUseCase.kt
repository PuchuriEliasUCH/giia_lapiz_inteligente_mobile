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

    /**
     * Crea una nueva sesión para un niño y ejercicio específicos.
     *
     * @param childId Identificador del niño.
     * @param exerciseId Identificador del ejercicio.
     * @return Result con [SessionResponse] o un error.
     */
    suspend fun createSession(childId: Int, exerciseId: Int): Result<SessionResponse> {
        return sessionRepository.createSession(childId, exerciseId)
    }

    /**
     * Obtiene el detalle de una sesión por su identificador.
     *
     * @param sessionId Identificador de la sesión.
     * @return Result con [SessionResponse] o un error.
     */
    suspend fun getSession(sessionId: Int): Result<SessionResponse> {
        return sessionRepository.getSession(sessionId)
    }

    /**
     * Obtiene el historial de sesiones de un niño.
     *
     * @param childId Identificador del niño.
     * @return Result con la lista de [SessionResponse] o un error.
     */
    suspend fun getChildSessions(childId: Int): Result<List<SessionResponse>> {
        return sessionRepository.getChildSessions(childId)
    }

    /**
     * Finaliza una sesión activa.
     *
     * @param sessionId Identificador de la sesión.
     * @param closeReason Motivo del cierre (por defecto "manual").
     * @return Result con [SessionResponse] o un error.
     */
    suspend fun endSession(sessionId: Int, closeReason: String = "manual"): Result<SessionResponse> {
        return sessionRepository.endSession(sessionId, closeReason)
    }
}
