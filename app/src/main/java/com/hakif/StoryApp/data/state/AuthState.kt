package com.hakif.StoryApp.data.state

import com.hakif.StoryApp.data.network.response.RegisterResponse

sealed interface AuthState {
    data object Loading : AuthState
    data class Success(val response: RegisterResponse) : AuthState
    data class Error(val message: String) : AuthState
    data object Idle : AuthState
}