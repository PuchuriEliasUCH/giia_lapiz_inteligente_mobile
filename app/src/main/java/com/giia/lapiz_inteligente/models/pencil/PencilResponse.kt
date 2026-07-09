package com.giia.lapiz_inteligente.models.pencil

data class PencilResponse(
    val pencil_id: Int,
    val device_uid: String,
    val name: String?,
    val status: String,
    val firmware_version: String?,
    val last_seen_at: String?,
    val created_at: String,
    val updated_at: String
)
