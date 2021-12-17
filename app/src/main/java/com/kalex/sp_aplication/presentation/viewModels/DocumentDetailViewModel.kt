package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.get_documentDetail.GetDocumentDetailUseCase
import com.kalex.sp_aplication.domain.use_case.get_documents.GetDocumentsUseCase
import com.kalex.sp_aplication.presentation.states.DocumentDetailState
import com.kalex.sp_aplication.presentation.states.DocumentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    private val getDocumentDetailUseCase: GetDocumentDetailUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = mutableStateOf(DocumentDetailState())
    val state: State<DocumentDetailState> = _state

    init {

        savedStateHandle.get<String>("idRegistro")?.let { registro ->
            getDocument(registro)
        }
    }

    private fun getDocument(iddoc: String) {
        getDocumentDetailUseCase(iddoc).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = DocumentDetailState(document = result.data)
                }
                is Resource.Error -> {
                    _state.value = DocumentDetailState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = DocumentDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}