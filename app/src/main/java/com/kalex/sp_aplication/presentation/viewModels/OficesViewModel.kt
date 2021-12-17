package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.get_ofices.GetOficesUseCase
import com.kalex.sp_aplication.presentation.states.OficeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OficesViewModel @Inject constructor(
    private val getOficesUseCase: GetOficesUseCase

): ViewModel(){

    private val _state = mutableStateOf(OficeState())
    val state: State<OficeState> = _state

    init {
        getOfices()

    }

    private fun getOfices() {
        getOficesUseCase().onEach { result ->
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