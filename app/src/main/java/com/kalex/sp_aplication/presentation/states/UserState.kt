package com.kalex.sp_aplication.presentation.states

import com.kalex.sp_aplication.data.remote.dto.Userdto
import com.kalex.sp_aplication.domain.model.User

data class UserState(
    val isLoading:Boolean = false,
    val user : Userdto?=null,
    val error :String = ""

)
