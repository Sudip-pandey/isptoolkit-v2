package com.pandey.isptoolkit.feature.speedtest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeedTestManager @Inject constructor() {

    data class SpeedTestResult(
        val downloadMbps: Double,
        val uploadMbps: Double,
        val latencyMs: Long,
        val jitterMs: Double,
        val error: String? = null
    )

    suspend fun measureDownloadSpeed(
        testUrl: String = "https://httpbin.org/stream-bytes/5000000",
        durationSeconds: Int = 5
    ): Double = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            val connection = URL(testUrl).openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            var bytesRead = 0L
            connection.getInputStream().use { input ->
                val buffer = ByteArray(8192)
                var n: Int
                while (input.read(buffer).also { n = it } != -1) {
                    bytesRead += n
                    if ((System.currentTimeMillis() - start) > durationSeconds * 1000L) break
                }
            }
            val elapsed = (System.currentTimeMillis() - start) / 1000.0
            if (elapsed > 0) (bytesRead * 8.0 / 1_000_000.0) / elapsed else 0.0
        } catch (e: Exception) {
            -1.0
        }
    }

    suspend fun measureLatency(host: String = "8.8.8.8"): Long = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            java.net.InetAddress.getByName(host).isReachable(3000)
            System.currentTimeMillis() - start
        } catch (e: Exception) {
            -1L
        }
    }
}
