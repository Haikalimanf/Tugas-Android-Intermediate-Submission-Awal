package com.hakif.StoryApp.data.repository

import com.hakif.StoryApp.data.network.response.LoginResponse
import com.hakif.StoryApp.data.network.response.RegisterResponse
import com.hakif.StoryApp.data.network.retrofit.ApiService
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

}