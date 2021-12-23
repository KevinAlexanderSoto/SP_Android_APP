package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.presentation.validations.Emailvalidation
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.dataStore.SettingsDataStore
import com.kalex.sp_aplication.domain.use_case.get_users.GetUserUseCase
import com.kalex.sp_aplication.presentation.states.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val settingsDataStore: SettingsDataStore,
    savedStateHandle: SavedStateHandle
):ViewModel(){

    private val _state = mutableStateOf(UserState())
    val state: State<UserState> = _state
    val savedStateHandle = savedStateHandle
    init {

        /*var iduser:String=""
        savedStateHandle.get<String>("idUsuario")?.let { Id ->
            iduser = Id
        }
        savedStateHandle.get<String>("clave")?.let { clave ->
            //getUser(iduser,clave)
        }*/

    }
    //Emailvalidation   MutableState<String>
     fun getUser(email:String , contraseña:String ) {
        var realcontraseña:String
        // po si el usuario no coloca contraseña
        if (contraseña ==""){
             realcontraseña = "no"
        }else{
             realcontraseña = contraseña
        }

        getUserUseCase(email,realcontraseña).onEach { result ->
            when (result) {
                is Resource.Success -> {

                    _state.value = UserState(user = result.data)
                }
                is Resource.Error -> {
                    _state.value = UserState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    //para guardar en preferences
    fun saveAll(nombre: String,correo: String,contraseña: String){
        viewModelScope.launch {
            settingsDataStore.saveAll(nombre,correo,contraseña)
        }
    }
}
