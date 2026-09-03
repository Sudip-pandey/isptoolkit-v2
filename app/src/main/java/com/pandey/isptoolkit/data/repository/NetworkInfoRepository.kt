package com.pandey.isptoolkit.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInfoRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class NetworkInfo(
        val isConnected: Boolean,
        val type: String,
        val downstreamBandwidthKbps: Int,
        val upstreamBandwidthKbps: Int
    )

    fun getNetworkInfo(): NetworkInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(network)
        return if (caps != null) {
            val type = when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                else -> "Unknown"
            }
            NetworkInfo(
                isConnected = true,
                type = type,
                downstreamBandwidthKbps = caps.linkDownstreamBandwidthKbps,
                upstreamBandwidthKbps = caps.linkUpstreamBandwidthKbps
            )
        } else {
            NetworkInfo(false, "None", 0, 0)
        }
    }
}
