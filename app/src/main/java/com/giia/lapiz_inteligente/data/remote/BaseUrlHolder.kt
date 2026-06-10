package com.giia.lapiz_inteligente.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseUrlHolder @Inject constructor() {

    @Volatile
    private var _baseUrl: String = "http://10.176.112.171:8001/"

    fun get(): String = _baseUrl

    fun set(url: String) {
        _baseUrl = url.trimEnd('/') + "/"
    }
}
