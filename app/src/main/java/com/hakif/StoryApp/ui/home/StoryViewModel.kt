package com.hakif.StoryApp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.repository.StoryRepository
import com.hakif.StoryApp.data.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    private val _getStoryState = MutableStateFlow<AuthState<GetStoryResponse>>(AuthState.Loading)
    val getStoryState: StateFlow<AuthState<GetStoryResponse>> get() = _getStoryState

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            _getStoryState.value = AuthState.Loading
            try {
                val response = storyRepository.getStories()
                _getStoryState.value = AuthState.Success(response)
            } catch (e: Exception) {
                _getStoryState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}