package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.post_document.PostDocumentUseCase
import com.kalex.sp_aplication.presentation.states.PostDocumentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostDocumentViewModel @Inject constructor(
    private val postDocumentUseCase: PostDocumentUseCase,

) : ViewModel() {
    private val _state = mutableStateOf(PostDocumentState())
    val state: State<PostDocumentState> = _state

    fun postDocument(body: RequestBody) {
        postDocumentUseCase(body).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = PostDocumentState(respuesta = result.data)
                }

                is Resource.Error -> {
                    _state.value = PostDocumentState(
                        error = result.message ?: "An unexpected error occured",
                    )
                }
                is Resource.Loading -> {
                    _state.value = PostDocumentState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
