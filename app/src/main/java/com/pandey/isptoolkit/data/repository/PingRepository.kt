package com.pandey.isptoolkit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PingRepository @Inject constructor() {

    data class PingResult(
        val host: String,
        val reachable: Boolean,
        val latencyMs: Long,
        val error: String? = null
    )

    suspend fun ping(host: String, timeoutMs: Int = 3000): PingResult = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(host).isReachable(timeoutMs)
            val latency = System.currentTimeMillis() - start
            PingResult(host, reachable, if (reachable) latency else -1)
        } catch (e: Exception) {
            PingResult(host, false, -1, e.message)
        }
    }

    suspend fun pingMultiple(host: String, count: Int = 5): List<PingResult> {
        return (1..count).map { ping(host) }
    }
}
