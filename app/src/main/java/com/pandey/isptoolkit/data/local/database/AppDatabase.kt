package com.pandey.isptoolkit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pandey.isptoolkit.data.local.dao.*
import com.pandey.isptoolkit.data.local.entity.*

@Database(
    entities = [
        SiteEntity::class,
        VisitEntity::class,
        DeviceEntity::class,
        NetworkScanEntity::class,
        DiagnosticSessionEntity::class,
        FiberCalculationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun siteDao(): SiteDao
    abstract fun deviceDao(): DeviceDao
    abstract fun networkScanDao(): NetworkScanDao
    abstract fun diagnosticSessionDao(): DiagnosticSessionDao
    abstract fun fiberCalculationDao(): FiberCalculationDao
}
