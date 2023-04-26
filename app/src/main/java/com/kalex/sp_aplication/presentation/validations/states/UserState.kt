package com.kalex.sp_aplication.presentation.validations.states

import com.kalex.sp_aplication.data.remote.dto.Userdto

data class UserState(
    val isLoading: Boolean = true,
    var user: Userdto? = null,
    val error: String = "",

)
