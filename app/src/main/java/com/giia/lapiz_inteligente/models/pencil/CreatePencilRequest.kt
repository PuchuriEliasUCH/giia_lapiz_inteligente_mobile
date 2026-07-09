package com.giia.lapiz_inteligente.models.pencil

data class CreatePencilRequest(
    val device_uid: String,
    val name: String? = null,
    val firmware_version: String? = null
)
