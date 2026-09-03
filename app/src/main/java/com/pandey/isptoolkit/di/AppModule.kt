package com.pandey.isptoolkit.di

import android.content.Context
import androidx.room.Room
import com.pandey.isptoolkit.data.local.dao.*
import com.pandey.isptoolkit.data.local.database.AppDatabase
import com.pandey.isptoolkit.data.discovery.LanDiscoveryRepository
import com.pandey.isptoolkit.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "isp_toolkit_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSiteDao(db: AppDatabase): SiteDao = db.siteDao()

    @Provides
    @Singleton
    fun provideDeviceDao(db: AppDatabase): DeviceDao = db.deviceDao()

    @Provides
    @Singleton
    fun provideNetworkScanDao(db: AppDatabase): NetworkScanDao = db.networkScanDao()

    @Provides
    @Singleton
    fun provideDiagnosticSessionDao(db: AppDatabase): DiagnosticSessionDao = db.diagnosticSessionDao()

    @Provides
    @Singleton
    fun provideFiberCalculationDao(db: AppDatabase): FiberCalculationDao = db.fiberCalculationDao()
}
