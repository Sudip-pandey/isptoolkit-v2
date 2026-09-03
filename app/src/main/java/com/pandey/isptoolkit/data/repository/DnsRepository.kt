package com.pandey.isptoolkit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DnsRepository @Inject constructor() {

    data class DnsResult(
        val hostname: String,
        val resolvedAddresses: List<String>,
        val latencyMs: Long,
        val error: String? = null
    )

    suspend fun resolve(hostname: String): DnsResult = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            val addresses = InetAddress.getAllByName(hostname).map { it.hostAddress ?: "Unknown" }
            val latency = System.currentTimeMillis() - start
            DnsResult(hostname, addresses, latency)
        } catch (e: Exception) {
            DnsResult(hostname, emptyList(), -1, e.message)
        }
    }
}
