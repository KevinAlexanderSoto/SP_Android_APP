package com.kalex.sp_aplication.presentation.validations.states

import com.kalex.sp_aplication.data.remote.dto.DocumentDto

data class DocumentDetailState(
    val isLoading: Boolean = false,
    val document: DocumentDto? = null,
    val error: String = "",
)
