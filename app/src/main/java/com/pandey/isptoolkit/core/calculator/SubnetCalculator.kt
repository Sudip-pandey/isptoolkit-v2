package com.pandey.isptoolkit.core.calculator

import java.net.InetAddress

object SubnetCalculator {

    data class SubnetResult(
        val ipAddress: String,
        val cidr: Int,
        val subnetMask: String,
        val networkAddress: String,
        val broadcastAddress: String,
        val firstHost: String,
        val lastHost: String,
        val totalHosts: Long,
        val usableHosts: Long
    )

    fun calculate(ipAddress: String, cidr: Int): SubnetResult? {
        return try {
            val ip = ipToLong(ipAddress)
            val mask = if (cidr == 0) 0L else ((-1L shl (32 - cidr)) and 0xFFFFFFFFL)
            val network = ip and mask
            val broadcast = network or mask.inv().and(0xFFFFFFFFL)
            val total = broadcast - network + 1
            val usable = if (cidr >= 31) total else (total - 2).coerceAtLeast(0)
            SubnetResult(
                ipAddress = ipAddress,
                cidr = cidr,
                subnetMask = longToIp(mask),
                networkAddress = longToIp(network),
                broadcastAddress = longToIp(broadcast),
                firstHost = if (cidr >= 31) longToIp(network) else longToIp(network + 1),
                lastHost = if (cidr >= 31) longToIp(broadcast) else longToIp(broadcast - 1),
                totalHosts = total,
                usableHosts = usable
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun ipToLong(ip: String): Long {
        val parts = ip.split(".")
        var result = 0L
        for (part in parts) {
            result = result shl 8 or part.toLong()
        }
        return result
    }

    private fun longToIp(ip: Long): String {
        return "${(ip shr 24) and 0xFF}.${(ip shr 16) and 0xFF}.${(ip shr 8) and 0xFF}.${ip and 0xFF}"
    }
}
