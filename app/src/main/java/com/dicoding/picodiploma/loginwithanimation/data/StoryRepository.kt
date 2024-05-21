package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.api.response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.api.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.api.response.StoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository {

    fun getPager(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(this, token) }
        ).liveData
    }

    suspend fun getStories(token: String, page: Int): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStories(page)
    }



    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStoriesWithLocation(1)
    }

    suspend fun uploadImage(imageFile: File, description: String, lat: Float?, lon: Float?, token: String
    ): FileUploadResponse {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", imageFile.name, imageBody)
        val latPart = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonPart = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.uploadImage(body, descriptionBody, latPart, lonPart)
    }
}