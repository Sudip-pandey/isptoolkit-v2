package com.pandey.isptoolkit.data.repository

import android.content.Context
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiAnalyzerRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class AccessPoint(
        val ssid: String,
        val bssid: String,
        val signalDbm: Int,
        val frequency: Int,
        val channel: Int,
        val capabilities: String,
        val signalLevel: Int
    )

    @Suppress("DEPRECATION")
    fun getScanResults(): List<AccessPoint> {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.scanResults.map { result ->
                val freq = result.frequency
                val channel = when {
                    freq in 2412..2484 -> (freq - 2407) / 5
                    freq in 5170..5825 -> (freq - 5000) / 5
                    else -> 0
                }
                AccessPoint(
                    ssid = result.SSID.ifEmpty { "<Hidden>" },
                    bssid = result.BSSID,
                    signalDbm = result.level,
                    frequency = freq,
                    channel = channel,
                    capabilities = result.capabilities,
                    signalLevel = WifiManager.calculateSignalLevel(result.level, 5)
                )
            }.sortedByDescending { it.signalDbm }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
