package com.hakif.StoryApp.data.repository

import android.util.Log
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.network.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
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
        Log.d("getStory", "getStories: $token")
        Log.d("getStory", "apiService: $apiService")
        return apiService.getStories(
            page,
            size,
            location
        )
    }
}