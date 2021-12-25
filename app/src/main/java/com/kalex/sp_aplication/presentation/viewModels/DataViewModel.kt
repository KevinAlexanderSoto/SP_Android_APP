package com.kalex.sp_aplication.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.data.dataStore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
):ViewModel() {

var nombre :String = ""
var correo :String = ""
var constraseña :String = ""
var inicio :Boolean = false

    val settingsPrefs = settingsDataStore.settingsPrefsFlow.onEach { result ->
        nombre = result.nombre
        correo = result.correo
        constraseña = result.contraseña
    }.launchIn(viewModelScope)

    fun saveAll(nombre: String,correo: String,contraseña: String){
        viewModelScope.launch {
            settingsDataStore.saveAll(nombre,correo,contraseña)
        }
    }
}