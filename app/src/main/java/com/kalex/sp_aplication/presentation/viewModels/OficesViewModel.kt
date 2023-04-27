package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.maps.model.LatLng
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.dataStore.SettingsDataStore
import com.kalex.sp_aplication.domain.use_case.get_ofices.GetOfficesUseCase
import com.kalex.sp_aplication.presentation.validations.states.OficeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OficesViewModel @Inject constructor(
    private val getOfficesUseCase: GetOfficesUseCase,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _state = mutableStateOf(OficeState())
    val state: State<OficeState> = _state
    var correo: String = ""

    var userLocation: LatLng = LatLng(6.217, -75.567)

    init {
        val settingsPrefs = settingsDataStore.settingsPrefsFlow.onEach { result ->
            correo = result.EMAIL
        }.launchIn(viewModelScope)

        getOfices()
    }

    private fun getOfices() {
        getOfficesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = OficeState(ofices = result.data)
                }
                is Resource.Error -> {
                    _state.value = OficeState(
                        error = result.message ?: "An unexpected error occured",
                    )
                }
                is Resource.Loading -> {
                    _state.value = OficeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
