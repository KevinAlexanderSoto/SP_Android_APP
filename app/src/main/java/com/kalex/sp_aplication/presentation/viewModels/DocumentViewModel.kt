package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.dataStore.SettingsDataStore
import com.kalex.sp_aplication.domain.use_case.get_documents.GetDocumentsUseCase
import com.kalex.sp_aplication.presentation.validations.states.DocumentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _state = mutableStateOf(DocumentState())
    val state: State<DocumentState> = _state
    var dato: String = ""
    init {
        settingsDataStore.settingsPrefsFlow.onEach { result ->
            dato = result.EMAIL
        }.launchIn(viewModelScope)
        getDocuments(dato)
    }

    fun getDocuments(email: String = "no") {
        getDocumentsUseCase(email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = DocumentState(document = result.data)
                }
                is Resource.Error -> {
                    _state.value = DocumentState(
                        error = result.message ?: "An unexpected error occured",
                    )
                }
                is Resource.Loading -> {
                    _state.value = DocumentState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
