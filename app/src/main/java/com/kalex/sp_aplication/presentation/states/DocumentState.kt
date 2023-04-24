package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.DocumentDetailDto

data class DocumentState(
    val isLoading: Boolean = false,
    val document: DocumentDetailDto? = null,
    val error: String = "",
)
