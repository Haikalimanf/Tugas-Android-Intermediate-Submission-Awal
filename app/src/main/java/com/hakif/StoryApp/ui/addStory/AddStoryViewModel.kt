package com.hakif.StoryApp.ui.addStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakif.StoryApp.data.network.response.RegisterResponse
import com.hakif.StoryApp.data.network.response.story.AddStoryResponse
import com.hakif.StoryApp.data.repository.StoryRepository
import com.hakif.StoryApp.data.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    private val _addStoryState = MutableStateFlow<AuthState<AddStoryResponse>>(AuthState.Idle)
    val addStoryState: StateFlow<AuthState<AddStoryResponse>> get() = _addStoryState

    fun addStory(description: RequestBody, photo: MultipartBody.Part) {
        _addStoryState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = storyRepository.addStory(description, photo)
                _addStoryState.value = AuthState.Success(response)
            } catch (e: Exception) {
                _addStoryState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}