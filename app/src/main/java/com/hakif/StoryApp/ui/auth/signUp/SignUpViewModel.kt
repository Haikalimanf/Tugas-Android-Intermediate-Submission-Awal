package com.hakif.StoryApp.ui.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hakif.StoryApp.data.network.response.RegisterResponse
import com.hakif.StoryApp.data.repository.AuthRepository
import com.hakif.StoryApp.data.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> get() = _registerState

    fun register(name: String, email: String, password: String) {
        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.register(name, email, password)
                _registerState.value = AuthState.Success(response)
            } catch (e: Exception) {
                var errorMessage = e.message ?: "Unknown error occurred"
                if (e is HttpException) {
                    try {
                        val errorBody = e.response()?.errorBody()?.string()
                        if (errorBody != null) {
                            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                            errorMessage = errorResponse.message
                        }
                    } catch (jsonError: Exception) {
                        errorMessage = "Error parsing response: ${e.code()}"
                    }
                }
                _registerState.value = AuthState.Error(errorMessage)
            }
        }
    }
}