package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.api.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesWithLocation: LiveData<List<ListStoryItem>> get() = _storiesWithLocation

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun getStoriesWithLocation() {
        _progressBarVisibility.value = true
        viewModelScope.launch {
            try {
                val userSession = userRepository.getSession().first()
                val response = storyRepository.getStoriesWithLocation(userSession!!.token)
                if (response.error == true) {
                    _errorMessage.value = response.message
                } else {
                    _storiesWithLocation.value = response.listStory.orEmpty().filterNotNull()
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _progressBarVisibility.value = false
            }
        }
    }
}