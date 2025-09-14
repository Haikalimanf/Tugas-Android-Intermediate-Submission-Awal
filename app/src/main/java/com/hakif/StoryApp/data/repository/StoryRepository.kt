package com.hakif.StoryApp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.response.story.AddStoryResponse
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.network.response.story.ListStoryItem
import com.hakif.StoryApp.data.network.retrofit.ApiConfig
import com.hakif.StoryApp.data.network.retrofit.ApiService
import com.hakif.StoryApp.data.paging.StoryPagingSource
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class StoryRepository @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val apiService: ApiService
) {
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return androidx.lifecycle.liveData {
            val token = dataStoreRepository.getToken().first() ?: ""
            val apiServiceWithToken = ApiConfig.getApiService(token)
            emitSource(
                Pager(
                    config = PagingConfig(pageSize = 3),
                    pagingSourceFactory = { StoryPagingSource(apiServiceWithToken) }
                ).liveData
            )
        }
    }

    suspend fun getStoriesWithLocation(): GetStoryResponse {
        return apiService.getStories(location = 1)
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