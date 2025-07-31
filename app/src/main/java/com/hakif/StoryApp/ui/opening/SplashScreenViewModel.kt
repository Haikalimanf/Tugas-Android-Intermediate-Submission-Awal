package com.hakif.StoryApp.ui.opening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
): ViewModel() {

    fun checkLoginStatus(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = dataStoreRepository.getToken().first()
            onResult(!token.isNullOrEmpty())
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStoreRepository.clearToken()
        }
    }

}