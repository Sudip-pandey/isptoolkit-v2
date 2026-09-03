package com.pandey.isptoolkit.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "visits",
    foreignKeys = [ForeignKey(entity = SiteEntity::class, parentColumns = ["id"], childColumns = ["siteId"], onDelete = ForeignKey.CASCADE)]
)
data class VisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val siteId: Long,
    val visitDate: Long = System.currentTimeMillis(),
    val technicianName: String,
    val notes: String = ""
)

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ipAddress: String,
    val macAddress: String = "Unknown",
    val hostname: String = "Unknown",
    val deviceType: String = "Unknown",
    val lastSeen: Long = System.currentTimeMillis()
)

@Entity(tableName = "network_scans")
data class NetworkScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ssid: String,
    val bssid: String,
    val signalDbm: Int,
    val frequency: Int,
    val channel: Int,
    val security: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "diagnostic_sessions")
data class DiagnosticSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val healthScore: Int,
    val summary: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "fiber_calculations")
data class FiberCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val inputDbm: Double,
    val outputDbm: Double,
    val lossDb: Double,
    val parameters: String,
    val timestamp: Long = System.currentTimeMillis()
)
