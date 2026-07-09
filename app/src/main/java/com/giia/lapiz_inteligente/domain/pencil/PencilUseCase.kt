package com.giia.lapiz_inteligente.domain.pencil

import com.giia.lapiz_inteligente.data.repository.PencilRepository
import com.giia.lapiz_inteligente.models.pencil.CreatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.PencilResponse
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilStatusRequest
import javax.inject.Inject

/**
 * Caso de uso para consultar y administrar lápices inteligentes.
 */
class PencilUseCase @Inject constructor(
    private val pencilRepository: PencilRepository
) {
    suspend fun getPencils(status: String? = null): Result<List<PencilResponse>> {
        return pencilRepository.getPencils(status)
    }

    suspend fun getPencil(pencilId: Int): Result<PencilResponse> {
        return pencilRepository.getPencil(pencilId)
    }

    suspend fun createPencil(request: CreatePencilRequest): Result<PencilResponse> {
        return pencilRepository.createPencil(request)
    }

    suspend fun updatePencil(pencilId: Int, request: UpdatePencilRequest): Result<PencilResponse> {
        return pencilRepository.updatePencil(pencilId, request)
    }

    suspend fun updatePencilStatus(
        pencilId: Int,
        request: UpdatePencilStatusRequest
    ): Result<PencilResponse> {
        return pencilRepository.updatePencilStatus(pencilId, request)
    }
}
