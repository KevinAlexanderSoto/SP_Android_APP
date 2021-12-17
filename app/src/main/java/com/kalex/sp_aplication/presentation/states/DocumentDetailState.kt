package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.DocumentDetailDto
import com.kalex.sp_aplication.data.remote.dto.DocumentDto

data class DocumentDetailState (
    val isLoading:Boolean = false,
    val document : DocumentDto? = null,
    val error :String = ""
)