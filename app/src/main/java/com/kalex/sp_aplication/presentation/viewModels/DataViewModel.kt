package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.data.dataStore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    init {
        settingsDataStore.settingsPrefsFlow.onEach { result ->
            _name.value = result.NAME
            _email.value = result.EMAIL
            _password.value = result.PASSWORD
        }.launchIn(viewModelScope)
    }
    fun saveAll(name: String, email: String, password: String) {
        viewModelScope.launch {
            settingsDataStore.saveAll(name, email, password)
        }
    }
    fun saveAuthenticationsCredentials(email: String, password: String) {
        viewModelScope.launch {
            settingsDataStore.saveLogin(email, password)
        }
    }
    fun saveUserName(name: String) {
        viewModelScope.launch {
            settingsDataStore.saveName(name)
        }
    }
}
