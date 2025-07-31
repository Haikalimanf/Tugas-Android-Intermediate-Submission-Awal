package com.hakif.StoryApp.data.state

import com.hakif.StoryApp.data.network.response.RegisterResponse

sealed class AuthState<out T> {
    object Idle : AuthState<Nothing>()
    object Loading : AuthState<Nothing>()
    data class Success<out T>(val data: T) : AuthState<T>()
    data class Error(val message: String) : AuthState<Nothing>()
}