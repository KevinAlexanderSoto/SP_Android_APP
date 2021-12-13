package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.domain.model.Document


data class DocumentState(
    val isLoading:Boolean = false,
    val document : DocumentDto? = null,
    val error :String = ""
)
