package com.pandey.isptoolkit.data.discovery

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanDiscoveryRepository @Inject constructor() {

    data class DiscoveredDevice(
        val ipAddress: String,
        val hostname: String,
        val reachable: Boolean,
        val latencyMs: Long
    )

    suspend fun scanSubnet(baseIp: String, timeoutMs: Int = 500): List<DiscoveredDevice> =
        withContext(Dispatchers.IO) {
            val prefix = baseIp.substringBeforeLast(".")
            (1..254).map { host ->
                async {
                    val ip = "$prefix.$host"
                    try {
                        val start = System.currentTimeMillis()
                        val addr = InetAddress.getByName(ip)
                        val reachable = addr.isReachable(timeoutMs)
                        val latency = System.currentTimeMillis() - start
                        if (reachable) {
                            DiscoveredDevice(ip, addr.canonicalHostName, true, latency)
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull().sortedBy {
                it.ipAddress.split(".").last().toIntOrNull() ?: 0
            }
        }
}
