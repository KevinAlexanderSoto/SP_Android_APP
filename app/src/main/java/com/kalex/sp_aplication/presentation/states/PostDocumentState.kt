package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.postDocumentDto

data class PostDocumentState(
    val isLoading: Boolean = false,
    val respuesta: postDocumentDto? = null,
    val error: String = "",
)
