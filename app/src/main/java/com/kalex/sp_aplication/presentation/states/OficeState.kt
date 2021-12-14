package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.OficeDto
import com.kalex.sp_aplication.domain.model.Ofice

data class OficeState(
    val isLoading:Boolean = false,
    val ofices: OficeDto? = null,
    val error:String = ""
)