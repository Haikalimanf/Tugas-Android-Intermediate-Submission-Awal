package com.hakif.StoryApp.ui.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakif.StoryApp.data.repository.AuthRepository
import com.hakif.StoryApp.data.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel()  {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> get() = _loginState

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                _loginState.value = AuthState.Success(response)
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

}