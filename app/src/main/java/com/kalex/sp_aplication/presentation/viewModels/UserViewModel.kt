package com.kalex.sp_aplication.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.domain.use_case.get_users.GetUserUseCase
import com.kalex.sp_aplication.presentation.validations.states.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(UserState())
    val state: State<UserState> = _state

    fun getUser(email: String, password: String) {
        getUserUseCase(email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = UserState(
                        user = result.data,
                        isLoading = false,
                    )
                }

                is Resource.Error -> {
                    _state.value = UserState(
                        error = result.message ?: "An unexpected error occured",
                        isLoading = false,
                    )
                }

                is Resource.Loading -> {
                    _state.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
