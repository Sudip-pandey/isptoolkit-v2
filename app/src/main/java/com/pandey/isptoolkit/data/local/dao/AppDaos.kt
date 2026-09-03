package com.pandey.isptoolkit.data.local.dao

import androidx.room.*
import com.pandey.isptoolkit.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Query("SELECT * FROM sites ORDER BY createdAt DESC")
    fun getAllSites(): Flow<List<SiteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: SiteEntity): Long

    @Delete
    suspend fun deleteSite(site: SiteEntity)
}

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices ORDER BY lastSeen DESC")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity): Long

    @Query("DELETE FROM devices")
    suspend fun deleteAll()
}

@Dao
interface NetworkScanDao {
    @Query("SELECT * FROM network_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<NetworkScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: NetworkScanEntity): Long

    @Query("DELETE FROM network_scans")
    suspend fun deleteAll()
}

@Dao
interface DiagnosticSessionDao {
    @Query("SELECT * FROM diagnostic_sessions ORDER BY timestamp DESC LIMIT 50")
    fun getSessions(): Flow<List<DiagnosticSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: DiagnosticSessionEntity): Long
}

@Dao
interface FiberCalculationDao {
    @Query("SELECT * FROM fiber_calculations ORDER BY timestamp DESC LIMIT 100")
    fun getCalculations(): Flow<List<FiberCalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calc: FiberCalculationEntity): Long

    @Delete
    suspend fun deleteCalculation(calc: FiberCalculationEntity)
}
