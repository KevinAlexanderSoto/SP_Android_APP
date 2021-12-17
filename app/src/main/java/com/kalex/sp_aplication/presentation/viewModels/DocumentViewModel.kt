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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel(){

    private val _state = mutableStateOf(DocumentState())
    val state: State<DocumentState> = _state

   init{
       println("keys guardadas"+savedStateHandle.keys())
       savedStateHandle.get<String>("correo")?.let {correo->
           getDocuments(correo)
       }

    }

    private fun getDocuments(email : String  ) {

        getDocumentsUseCase(email).onEach { result ->
            println("DENTRO DE FUNCION ")
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