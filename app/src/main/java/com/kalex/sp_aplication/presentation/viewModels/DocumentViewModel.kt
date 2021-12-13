package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.get_documents.GetDocumentsUseCase
import com.kalex.sp_aplication.presentation.states.DocumentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = mutableStateOf(DocumentState())
    val state: State<DocumentState> = _state

    init {
        var iddoc:Int=0
        savedStateHandle.get<Int>("idRegistro")?.let { Id ->
            iddoc = Id
        }
        savedStateHandle.get<String>("correo")?.let { email ->
            getUser(iddoc,email)
        }
    }

    private fun getUser(iddoc: Int,email: String) {
        getDocumentsUseCase(iddoc,email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = DocumentState(document = result.data)
                }
                is Resource.Error -> {
                    _state.value = DocumentState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = DocumentState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}