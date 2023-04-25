package com.kalex.sp_aplication.presentation.validations.states

import com.kalex.sp_aplication.domain.model.Ofice

data class OficeState(
    val isLoading: Boolean = false,
    val ofices: Ofice? = null,
    val error: String = "",
)
