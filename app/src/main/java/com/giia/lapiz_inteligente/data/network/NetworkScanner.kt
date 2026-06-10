package com.giia.lapiz_inteligente.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkScanner @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .build()

    private val concurrencyLimit = Semaphore(30)

    suspend fun discoverBackend(port: Int = 8001, path: String = "health"): String? {
        val localIp = getLocalIpAddress() ?: return null
        val subnet = localIp.substringBeforeLast(".")

        return coroutineScope {
            val jobs = (1..254).map { i ->
                async(Dispatchers.IO) {
                    concurrencyLimit.withPermit {
                        val ip = "$subnet.$i"
                        try {
                            val url = "http://$ip:$port/$path"
                            val request = Request.Builder().url(url).build()
                            val response = client.newCall(request).execute()
                            if (response.isSuccessful) ip else null
                        } catch (_: Exception) {
                            null
                        }
                    }
                }
            }
            jobs.awaitAll().firstOrNull { it != null }
        }
    }

    private fun getLocalIpAddress(): String? {
        return try {
            NetworkInterface.getNetworkInterfaces()?.asSequence()
                ?.filter { intf ->
                    intf.isUp &&
                    !intf.isLoopback &&
                    intf.name != "docker0" &&
                    intf.name != "virbr0" &&
                    !intf.name.startsWith("docker")
                }
                ?.flatMap { it.inetAddresses.asSequence() }
                ?.find { addr ->
                    addr is InetAddress &&
                    !addr.isLoopbackAddress &&
                    addr.hostAddress?.contains(".") == true &&
                    !isPrivateOrVirtual(addr.hostAddress)
                }
                ?.hostAddress
        } catch (_: Exception) {
            null
        }
    }

    private fun isPrivateOrVirtual(ip: String?): Boolean {
        if (ip == null) return false
        val parts = ip.split(".").mapNotNull { it.toIntOrNull() }
        if (parts.size != 4) return false
        return when {
            parts[0] == 10 -> false
            parts[0] == 172 && parts[1] in 16..31 -> true
            parts[0] == 192 && parts[1] == 168 -> false
            parts[0] == 100 && parts[1] in 64..127 -> false
            else -> false
        }
    }
}
