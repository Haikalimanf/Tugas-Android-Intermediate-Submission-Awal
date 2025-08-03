package com.hakif.StoryApp.data.repository

import com.hakif.StoryApp.data.network.response.LoginResponse
import com.hakif.StoryApp.data.network.response.RegisterResponse
import com.hakif.StoryApp.data.network.retrofit.ApiConfig
import javax.inject.Inject


class AuthRepository @Inject constructor() {
    suspend fun login(email: String, password: String): LoginResponse {
        val apiService = ApiConfig.getApiService("")
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        val apiService = ApiConfig.getApiService("")
        return apiService.register(name, email, password)
    }
}