package com.kalex.sp_aplication.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kalex.sp_aplication.SpApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val SETTING_PREFERENCES_NAME: String = "settings_preferences"

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: SpApplication,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        SETTING_PREFERENCES_NAME,
    )

    val settingsPrefsFlow: Flow<DataNeed> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val name = preferences[NAME] ?: ""
            val email = preferences[EMAIL] ?: ""
            val password = preferences[PASSWORD] ?: ""
            DataNeed(
                NAME = name,
                EMAIL = email,
                PASSWORD = password,
            )
        }

    suspend fun saveName(nuevo: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME] = nuevo
        }
    }
    suspend fun saveAll(nombre: String, correo: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME] = nombre
            preferences[EMAIL] = correo
            preferences[PASSWORD] = password
        }
    }

    suspend fun saveLogin(email: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
        }
    }

    companion object {
        val NAME = stringPreferencesKey("NAME")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
    }

    data class DataNeed(
        val NAME: String,
        val EMAIL: String,
        val PASSWORD: String,
    )
}
