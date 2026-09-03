package com.pandey.isptoolkit.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "isp_prefs")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val technicianNameKey = stringPreferencesKey("technician_name")
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val technicianName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[technicianNameKey] ?: ""
    }

    val darkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[darkModeKey] ?: true
    }

    suspend fun setTechnicianName(name: String) {
        context.dataStore.edit { prefs -> prefs[technicianNameKey] = name }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[darkModeKey] = enabled }
    }
}
