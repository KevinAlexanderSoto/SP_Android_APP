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
            val nombre = preferences[NOMBRE] ?: ""
            val correo = preferences[CORREO] ?: ""
            val contraseña = preferences[CONTRASEÑA] ?: ""
            DataNeed(
                nombre = nombre,
                correo = correo,
                contraseña = contraseña,
            )
        }

    suspend fun saveNombre(nuevo: String) {
        context.dataStore.edit { preferences ->
            preferences[NOMBRE] = nuevo
        }
    }
    suspend fun saveAll(nombre: String, correo: String, contraseña: String) {
        context.dataStore.edit { preferences ->
            preferences[NOMBRE] = nombre
            preferences[CORREO] = correo
            preferences[CONTRASEÑA] = contraseña
        }
    }

    suspend fun saveLogin(correo: String, contraseña: String) {
        context.dataStore.edit { preferences ->
            preferences[CORREO] = correo
            preferences[CONTRASEÑA] = contraseña
        }
    }

    companion object {
        val NOMBRE = stringPreferencesKey("nombre")
        val CORREO = stringPreferencesKey("correo")
        val CONTRASEÑA = stringPreferencesKey("contraseña")
    }

    data class DataNeed(
        val nombre: String,
        val correo: String,
        val contraseña: String,
    )
}
