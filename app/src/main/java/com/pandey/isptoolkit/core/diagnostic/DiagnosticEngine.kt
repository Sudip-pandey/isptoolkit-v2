package com.pandey.isptoolkit.core.diagnostic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

object DiagnosticEngine {

    data class DiagnosticResult(
        val testName: String,
        val passed: Boolean,
        val value: String,
        val detail: String
    )

    data class DiagnosticSession(
        val results: List<DiagnosticResult>,
        val healthScore: Int,
        val summary: String
    )

    suspend fun runFullDiagnostic(gatewayIp: String = "8.8.8.8"): DiagnosticSession =
        withContext(Dispatchers.IO) {
            val tests = listOf(
                async { testDns("google.com") },
                async { testDns("1.1.1.1") },
                async { testPing("8.8.8.8") },
                async { testPing("1.1.1.1") },
                async { testTcpPort("google.com", 80) }
            )
            val results = tests.awaitAll()
            val passed = results.count { it.passed }
            val score = (passed * 100 / results.size.coerceAtLeast(1))
            val summary = when {
                score >= 90 -> "Network is healthy"
                score >= 70 -> "Network issues detected"
                score >= 50 -> "Significant network problems"
                else -> "Network severely degraded"
            }
            DiagnosticSession(results, score, summary)
        }

    private suspend fun testDns(host: String): DiagnosticResult = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            InetAddress.getByName(host)
            val ms = System.currentTimeMillis() - start
            DiagnosticResult("DNS: $host", true, "${ms}ms", "Resolved in ${ms}ms")
        } catch (e: Exception) {
            DiagnosticResult("DNS: $host", false, "Failed", e.message ?: "Unknown error")
        }
    }

    private suspend fun testPing(host: String): DiagnosticResult = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            val reached = InetAddress.getByName(host).isReachable(3000)
            val ms = System.currentTimeMillis() - start
            DiagnosticResult(
                "Ping: $host", reached,
                if (reached) "${ms}ms" else "Timeout",
                if (reached) "Reachable in ${ms}ms" else "Host unreachable"
            )
        } catch (e: Exception) {
            DiagnosticResult("Ping: $host", false, "Error", e.message ?: "Unknown error")
        }
    }

    private suspend fun testTcpPort(host: String, port: Int): DiagnosticResult =
        withContext(Dispatchers.IO) {
            try {
                val start = System.currentTimeMillis()
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), 3000)
                }
                val ms = System.currentTimeMillis() - start
                DiagnosticResult("TCP $host:$port", true, "${ms}ms", "Connected in ${ms}ms")
            } catch (e: Exception) {
                DiagnosticResult("TCP $host:$port", false, "Failed", e.message ?: "Connection refused")
            }
        }
}
