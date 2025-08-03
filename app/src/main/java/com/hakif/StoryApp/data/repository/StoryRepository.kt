package com.hakif.StoryApp.data.repository

import android.util.Log
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.response.story.AddStoryResponse
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.network.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun getStories(
        page: Int? = 1,
        size: Int? = 10,
        location: Int? = 0
    ): GetStoryResponse {
        val token = dataStoreRepository.getToken().first() ?: ""
        val apiService = ApiConfig.getApiService(token)
        return apiService.getStories(
            page,
            size,
            location
        )
    }

    suspend fun addStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): AddStoryResponse {
        val token = dataStoreRepository.getToken().first() ?: ""
        val apiService = ApiConfig.getApiService(token)
        return apiService.addStory(description, photo, lat, lon)
    }
}