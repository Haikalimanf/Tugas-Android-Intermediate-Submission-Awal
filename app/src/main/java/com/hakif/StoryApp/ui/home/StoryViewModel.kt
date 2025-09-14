package com.hakif.StoryApp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.response.story.GetStoryResponse
import com.hakif.StoryApp.data.network.response.story.ListStoryItem
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

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}