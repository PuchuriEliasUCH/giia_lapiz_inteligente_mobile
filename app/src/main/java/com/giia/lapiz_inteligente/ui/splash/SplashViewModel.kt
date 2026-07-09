package com.giia.lapiz_inteligente.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giia.lapiz_inteligente.data.datastore.SessionManager
import com.giia.lapiz_inteligente.data.network.NetworkScanner
import com.giia.lapiz_inteligente.data.remote.BaseUrlHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed interface SplashState {
    data object CheckingSession : SplashState
    data object SearchingServer : SplashState
    data class ServerFound(val isLoggedIn: Boolean) : SplashState
    data class Error(val message: String) : SplashState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val baseUrlHolder: BaseUrlHolder,
    private val networkScanner: NetworkScanner
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.CheckingSession)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val healthCheckClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            val cachedUrl = sessionManager.backendUrl.first()
            if (cachedUrl != null) {
                baseUrlHolder.set(cachedUrl)
                val alive = withContext(Dispatchers.IO) {
                    try {
                        val request = okhttp3.Request.Builder()
                            .url("${cachedUrl.trimEnd('/')}/health")
                            .build()
                        healthCheckClient.newCall(request).execute().use { response ->
                            response.isSuccessful
                        }
                    } catch (_: Exception) {
                        false
                    }
                }
                if (alive) {
                    val token = sessionManager.token.first()
                    _state.value = SplashState.ServerFound(token != null)
                    return@launch
                }
                sessionManager.clearBackendUrl()
            }

            _state.value = SplashState.SearchingServer
            val discoveredIp = networkScanner.discoverBackend()
            if (discoveredIp != null) {
                val url = "http://$discoveredIp:8001/"
                baseUrlHolder.set(url)
                sessionManager.saveBackendUrl(url)
                val token = sessionManager.token.first()
                _state.value = SplashState.ServerFound(token != null)
            } else {
                _state.value = SplashState.Error(
                    "No se encontró el servidor. Verifica que el backend esté corriendo " +
                    "y conectado a la misma red."
                )
            }
        }
    }

    fun retry() {
        _state.value = SplashState.CheckingSession
        initialize()
    }

    fun connectManual(ip: String) {
        viewModelScope.launch {
            _state.value = SplashState.SearchingServer
            val url = "http://$ip:8001/"
            val alive = withContext(Dispatchers.IO) {
                try {
                    val request = okhttp3.Request.Builder()
                        .url("${url.trimEnd('/')}/health")
                        .build()
                    healthCheckClient.newCall(request).execute().use { response ->
                        response.isSuccessful
                    }
                } catch (_: Exception) {
                    false
                }
            }
            if (alive) {
                baseUrlHolder.set(url)
                sessionManager.saveBackendUrl(url)
                val token = sessionManager.token.first()
                _state.value = SplashState.ServerFound(token != null)
            } else {
                _state.value = SplashState.Error(
                    "No se pudo conectar a $ip:8001. Verifica que la IP sea correcta, " +
                    "que el firewall permita el puerto y que el backend esté corriendo."
                )
            }
        }
    }
}
