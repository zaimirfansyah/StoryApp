package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.api.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModel() {

    private var currentStories: LiveData<PagingData<ListStoryItem>>? = null

    fun getSession(): LiveData<UserModel?> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    suspend fun getStories(): LiveData<PagingData<ListStoryItem>> {
        val userModel = userRepository.getSession().firstOrNull()

        return if (userModel != null) {
            val token = userModel.token
            val newStories = storyRepository.getPager(token)
                .cachedIn(viewModelScope)
            currentStories = newStories
            newStories
        } else {
            MutableLiveData()
        }
    }
}

