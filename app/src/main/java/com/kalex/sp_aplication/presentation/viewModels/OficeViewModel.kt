package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.get_ofice.GetOficeUseCase
import com.kalex.sp_aplication.presentation.states.OficeState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OficeViewModel@Inject constructor(
    private val getOficeUseCase: GetOficeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = mutableStateOf(OficeState())
    val state: State<OficeState> = _state

    init {
        savedStateHandle.get<String>("ciudad")?.let { ciudad ->
            getOfice(ciudad)
        }
    }

    private fun getOfice(ciudad:String) {
        getOficeUseCase(ciudad).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = OficeState(ofices = result.data)
                }
                is Resource.Error -> {
                    _state.value = OficeState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = OficeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}