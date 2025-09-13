package com.hakif.StoryApp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.repository.StoryRepository
import com.hakif.StoryApp.data.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _getLocationStoryState = MutableStateFlow<AuthState<GetStoryResponse>>(AuthState.Loading)
    val getLocationStoryState: StateFlow<AuthState<GetStoryResponse>> get() = _getLocationStoryState

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            _getLocationStoryState.value = AuthState.Loading
            try {
                val response = storyRepository.getStories()
                _getLocationStoryState.value = AuthState.Success(response)
            } catch (e: Exception) {
                _getLocationStoryState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

}